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
import java.util.Optional;

@Stateless
public class RefuellingService {
    @Inject
    FuelStockStore stockStore;
    @Inject
    VehicleStore vehicleStore;
    @Inject
    private RefuellingStore store;
    @Inject
    private EntityManager em;
    @Inject
    private Event<FillUpEvent> fillUpEvent;

    public void add(final String vehicleName, final String ownerName, final RefuellingMeta meta) {
        // TODO handle previous additions /recalculate ...
        final Vehicle vehicle = this.vehicleStore.getVehicleByNameAndOwnerEmail(ownerName, vehicleName)
                .orElseThrow(() -> new IllegalStateException("No such vehicle."));
        if (meta.litresToStock > 0D) {
            this.stockStore.addition(vehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
        }
        if (meta.litresFromStock > 0D) {
            this.stockStore.release(vehicle, meta.date, meta.litresFromStock);
        }

        Refuelling refuelling;
        if (meta.isFull) {
            refuelling = this.store.storeFillUp(vehicle, meta.eurosPerLitre,
                    meta.litresToTank, meta.kilometre, meta.memo, meta.date);
            final FillUpEvent event = new FillUpEvent().setRefuelling(refuelling);
            this.fillUpEvent.fire(event);
        } else {
            refuelling = this.store.storePartialRefueling(vehicle, meta.eurosPerLitre, meta.litresToTank,
                    meta.memo, meta.date);
        }
        vehicle.getRefuellings().add(refuelling);
        this.em.merge(vehicle);
    }

    public void remove(Refuelling refuelling) {
        Optional<Refuelling> possibleNextFillUp = this.store.getFillUpAfter(refuelling.getDateRefuelled());
        possibleNextFillUp.ifPresent(nextFillUp -> {
            // it is required to update the next refuelling since previous partials must be re-considered
            FillUpEvent event = new FillUpEvent().setRefuelling(nextFillUp);
            fillUpEvent.fire(event);
        });
        this.store.remove(refuelling);
    }
}
