package biz.suckow.fuelservice.business.refuelling.boundary;

/*
 * #%L
 * fuel
 * %%
 * Copyright (C) 2014 Suckow.biz
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

import biz.suckow.fuelservice.business.refuelling.control.FillUpEventGun;
import biz.suckow.fuelservice.business.refuelling.control.FuelStockStore;
import biz.suckow.fuelservice.business.refuelling.control.RefuellingStore;
import biz.suckow.fuelservice.business.refuelling.control.VehicleLocator;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class RefuellingService {
    @Inject
    private FillUpEventGun gun;
    @Inject
    private RefuellingStore refuellingStore;
    @Inject
    private FuelStockStore stockStore;
    // TODO: instead move locator to vehicle control and have a vehicle service here
    @Inject
    private VehicleLocator locator;

    public void add(final String vehicleName, final String ownerName, final RefuellingMeta meta) {
        // TODO handle previous additions /recalculate ...
        final Optional<Vehicle> possibleVehicle = this.locator.getVehicle(ownerName, vehicleName);
        final Vehicle vehicle = possibleVehicle.orElseThrow(() -> new IllegalStateException("Vehicle missing."));
        if (meta.litresToStock > 0D) {
            this.stockStore.addition(vehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
        }
        if (meta.litresFromStock > 0D) {
            this.stockStore.release(possibleVehicle.get(), meta.date, meta.litresFromStock);
        }
        if (meta.isFull) {
            final Refuelling refuelling = this.refuellingStore.storeFillUp(possibleVehicle.get(), meta.eurosPerLitre,
                    meta.litresToTank, meta.kilometre, meta.memo, meta.date);
            this.gun.fire(refuelling.getId());
        } else {
            this.refuellingStore.storePartialRefueling(possibleVehicle.get(), meta.eurosPerLitre, meta.litresToTank,
                    meta.memo, meta.date);
        }
    }
}
