package biz.suckow.fuelservice.business.refueling.control;

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

import javax.inject.Inject;

import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.refueling.entity.RefuelingMeta;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class RefuelingService {
    private FillUpEventGun gun;
    private RefuelingStore refuelingStore;
    private FuelStockStore stockStore;
    private VehicleLocator vehicleLocator;

    @Inject
    public RefuelingService(RefuelingStore refuelingStore, FuelStockStore stockStore, FillUpEventGun gun,
	    VehicleLocator vehicleLocator) {
	this.refuelingStore = refuelingStore;
	this.stockStore = stockStore;
	this.gun = gun;
	this.vehicleLocator = vehicleLocator;
    }

    public void add(String vehicleName, String ownerName, RefuelingMeta meta) {
	// TODO handle previous additions /recalculate ...
	Optional<Vehicle> possibleVehicle = this.vehicleLocator.getVehicle(ownerName, vehicleName);
	Preconditions.checkState(possibleVehicle.isPresent());
	if (meta.litresToStock > 0D) {
	    this.stockStore.addition(possibleVehicle.get(), meta.date, meta.eurosPerLitre, meta.litresToStock);
	}
	if (meta.litresFromStock > 0D) {
	    this.stockStore.release(possibleVehicle.get(), meta.date, meta.litresFromStock);
	}
	if (meta.isFull) {
	    Refueling refueling = this.refuelingStore.storeFillUp(possibleVehicle.get(), meta.eurosPerLitre,
		    meta.litresToTank, meta.kilometre, meta.memo, meta.date);
	    this.gun.fire(refueling.getId());
	} else {
	    this.refuelingStore.storePartialRefueling(possibleVehicle.get(), meta.eurosPerLitre, meta.litresToTank,
		    meta.memo, meta.date);
	}
    }
}
