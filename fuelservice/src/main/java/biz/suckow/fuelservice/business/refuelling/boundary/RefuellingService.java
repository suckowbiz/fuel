package biz.suckow.fuelservice.business.refuelling.boundary;

/*
 * #%L
 * fuelservice
 * %%
 * Copyright (C) 2014 - 2015 Suckow.biz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import biz.suckow.fuelservice.business.consumption.entity.FillUpEvent;
import biz.suckow.fuelservice.business.refuelling.control.FuelStockStore;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.vehicle.boundary.VehicleStore;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

@Stateless
public class RefuellingService {
    @Inject
    private FuelStockStore stockStore;

    @Inject
    private VehicleStore vehicleStore;

    @Inject
    private RefuellingStore refuellingStore;

    @Inject
    private EntityManager em;

    @Inject
    private Event<FillUpEvent> fillUpEvent;

    public void add(final String vehicleName, final String ownerName, final RefuellingMeta meta) {
        final Vehicle vehicle = this.vehicleStore.getVehicleByNameAndOwnerEmail(ownerName, vehicleName)
                .orElseThrow(() -> new IllegalStateException("No such vehicle."));
        updateStock(meta, vehicle);

        Refuelling refuelling;
        if (meta.isFull) {
            refuelling = this.refuellingStore.storeFillUp(vehicle, meta.eurosPerLitre, meta.litresToTank,
                    meta.kilometre, meta.memo, meta.date);
            final FillUpEvent event = new FillUpEvent().setRefuelling(refuelling);
            this.fillUpEvent.fire(event);
        } else {
            refuelling = this.refuellingStore.storePartialRefueling(vehicle, meta.eurosPerLitre, meta.litresToTank,
                    meta.memo, meta.date);
        }
        vehicle.getRefuellings().add(refuelling);
        this.em.merge(vehicle);

        // fire fill up for next refuelling to recalculate fuel consumption considering the actual addition
        updateNextFillUpConsumptionIfRequired(refuelling.getDateRefuelled());
    }

    public void remove(final Refuelling refuelling) {
        final Optional<Refuelling> possibleNextFillUp = this.refuellingStore
                .getFillUpAfter(refuelling.getDateRefuelled());
        possibleNextFillUp.ifPresent(nextFillUp -> {
            // it is required to update the next refuelling since previous partials must be re-considered
            updateNextFillUpConsumptionIfRequired(nextFillUp.getDateRefuelled());
        });
        this.refuellingStore.remove(refuelling);
    }

    private void updateNextFillUpConsumptionIfRequired(final Date referenceRefuelling) {
        final Optional<Refuelling> possibleNextFillUp = this.refuellingStore.getFillUpAfter(referenceRefuelling);
        possibleNextFillUp.ifPresent(refuelling -> {
            final FillUpEvent event = new FillUpEvent().setRefuelling(refuelling);
            fillUpEvent.fire(event);
        });
    }

    private void updateStock(final RefuellingMeta meta, final Vehicle vehicle) {
        if (meta.litresToStock > 0D) {
            this.stockStore.addition(vehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
        }
        if (meta.litresFromStock > 0D) {
            this.stockStore.release(vehicle, meta.date, meta.litresFromStock);
        }
    }
}
