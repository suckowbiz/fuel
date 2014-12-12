package biz.suckow.fuel.business.refueling.control;

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

import java.util.Date;

import javax.inject.Inject;

import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class RefuelingService {
    private FillUpEventGun gun;
    private RefuelingStore refuelingStore;
    private AdditionStore additionStore;

    @Inject
    public RefuelingService(RefuelingStore refuelingStore, AdditionStore additionStore, FillUpEventGun gun) {
	this.refuelingStore = refuelingStore;
	this.additionStore = additionStore;
	this.gun = gun;
    }

    public void fullTankRefuel(final Vehicle vehicle, final Double kilometre, final Double litres,
	    final Double eurosPerLitre, final Date date, final String memo) {
	Refueling refueling = this.refuelingStore.storeFillUp(eurosPerLitre, litres, kilometre, memo, date, vehicle);
	this.gun.fire(refueling.getId());
    }

    public void partialTankRefuel(final Vehicle vehicle, final Double litres, final Double euros, final Date date,
	    final String memo) {
	this.refuelingStore.storePartialRefueling(euros, litres, memo, date, vehicle);
    }

    public void fullTankAndStockRefuel(final Vehicle vehicle, final Double kilometre, final Double litresTank,
	    final Double litresStock, final Double euros, final Date date, final String memo) {
	this.fullTankRefuel(vehicle, kilometre, litresTank, euros, date, memo);
	this.stockAddition(vehicle, litresStock, euros, date, memo);
    }

    public void stockAddition(final Vehicle vehicle, final Double litres, final Double euros, final Date date,
	    final String memo) {
	this.additionStore.store(date,euros,litres,memo);
    }
}
