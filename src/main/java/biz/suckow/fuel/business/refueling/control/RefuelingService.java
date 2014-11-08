/*
 * Copyright 2014 Tobias Suckow.
 *
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
 */
package biz.suckow.fuel.business.refueling.control;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class RefuelingService {
    @PersistenceContext
    private EntityManager em;

    public Vehicle fullTankRefuel(final Vehicle vehicle,
	    final Double kilometres, final Double litres,
	    final Double eurosPerLitre, final Date date, final String memo) {
	final Refueling refueling = new Refueling.Builder()
	.eurosPerLitre(eurosPerLitre).litres(litres).memo(memo)
	.dateRefueled(date).fillUp(true).build();
	this.em.persist(refueling);

	vehicle.addRefueling(refueling);
	return this.em.merge(vehicle);
    }

    public Vehicle partialTankRefuel(final Vehicle vehicle,
	    final Double litres, final Double euros, final Date date,
	    final String memo) {
	final Refueling refueling = new Refueling.Builder().litres(litres)
		.eurosPerLitre(euros).dateRefueled(date).memo(memo).build();
	this.em.persist(refueling);

	vehicle.addRefueling(refueling);
	return this.em.merge(vehicle);
    }

    public void fullTankAndStockRefuel(final Vehicle vehicle,
	    final Double kilometers, final Double litresTank,
	    final Double litresStock, final Double euros, final Date date,
	    final String memo) {
	final Vehicle mergedVehicle = this.fullTankRefuel(vehicle, kilometers,
		litresTank, euros, date, memo);
	this.stockRefuel(mergedVehicle, litresStock, euros, date, null);
    }

    public void stockRefuel(final Vehicle vehicle, final Double litres,
	    final Double euros, final Date date, final String memo) {
	final Refueling stockRefueling = new Refueling.Builder().dateRefueled(date)
		.litres(litres).memo(memo).build();
	this.em.persist(stockRefueling);

	vehicle.getFuelStock().addRefueling(stockRefueling);
	this.em.merge(vehicle);
    }
}
