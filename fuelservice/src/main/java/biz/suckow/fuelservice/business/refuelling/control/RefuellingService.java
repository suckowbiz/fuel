package biz.suckow.fuelservice.business.refuelling.control;

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

import java.util.Optional;

import javax.inject.Inject;

import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class RefuellingService {
    private final FillUpEventGun gun;
    private final RefuellingStore refuellingStore;
    private final FuelStockStore stockStore;
    private final VehicleLocator vehicleLocator;

    @Inject
    public RefuellingService(final RefuellingStore refuellingStore, final FuelStockStore stockStore,
							 final FillUpEventGun gun, final VehicleLocator vehicleLocator) {
	this.refuellingStore = refuellingStore;
	this.stockStore = stockStore;
	this.gun = gun;
	this.vehicleLocator = vehicleLocator;
    }

    public void add(final String vehicleName, final String ownerName, final RefuellingMeta meta) {
	// TODO handle previous additions /recalculate ...
	final Optional<Vehicle> possibleVehicle = this.vehicleLocator.getVehicle(ownerName, vehicleName);
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
