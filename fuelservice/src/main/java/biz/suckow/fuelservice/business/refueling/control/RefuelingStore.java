package biz.suckow.fuelservice.business.refueling.control;

/*
 * #%L
 * fuelservice
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
import javax.persistence.EntityManager;

import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class RefuelingStore {
    private final EntityManager em;

    @Inject
    public RefuelingStore(final EntityManager em) {
	this.em = em;
    }

    public Refueling storeFillUp(final Vehicle vehicle, final Double eurosPerLitre, final Double litres, final Double kilometre, final String memo,
	    final Date date) {
	final Refueling result = new Refueling.Builder().eurosPerLitre(eurosPerLitre).litres(litres)
		.kilometre(kilometre).memo(memo).dateRefueled(date).fillUp(true).vehicle(vehicle).build();
	this.em.persist(result);
	return result;
    }

    public void storePartialRefueling(final Vehicle vehicle, final Double euros, final Double litres, final String memo, final Date date) {
	final Refueling refueling = new Refueling.Builder().litres(litres).eurosPerLitre(euros).dateRefueled(date)
		.memo(memo).vehicle(vehicle).build();
	this.em.persist(refueling);
    }

}
