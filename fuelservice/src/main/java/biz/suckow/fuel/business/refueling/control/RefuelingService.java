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

import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.consumption.entity.FillUpEvent;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

// TODO test
public class RefuelingService {
    @Inject
    private EntityManager em;

    @Inject
    private Event<FillUpEvent> fillUpEvent;

    public void fullTankRefuel(final Vehicle vehicle, final Double kilometre, final Double litres,
	    final Double eurosPerLitre, final Date date, final String memo) {
	final Refueling refueling = new Refueling.Builder().eurosPerLitre(eurosPerLitre).litres(litres)
		.kilometre(kilometre).memo(memo).dateRefueled(date).fillUp(true).vehicle(vehicle).build();
	this.em.persist(refueling);

	final FillUpEvent event = new FillUpEvent().setRefuelingId(refueling.getId());
	this.fillUpEvent.fire(event);
    }

    public void partialTankRefuel(final Vehicle vehicle, final Double litres, final Double euros, final Date date,
	    final String memo) {
	final Refueling refueling = new Refueling.Builder().litres(litres).eurosPerLitre(euros).dateRefueled(date)
		.memo(memo).vehicle(vehicle).build();
	this.em.persist(refueling);
    }

    public void fullTankAndStockRefuel(final Vehicle vehicle, final Double kilometre, final Double litresTank,
	    final Double litresStock, final Double euros, final Date date, final String memo) {
	this.fullTankRefuel(vehicle, kilometre, litresTank, euros, date, memo);
	this.stockAddition(vehicle, litresStock, euros, date, null);
    }

    public void stockAddition(final Vehicle vehicle, final Double litres, final Double euros, final Date date,
	    final String memo) {
	final StockAddition addition = new StockAddition().setDateAdded(date).setEurosPerLitre(euros).setLitres(litres)
		.setMemo(memo);
	this.em.persist(addition);
    }
}