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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class RefuelingLocator {
    private EntityManager em;

    @Inject
    public RefuelingLocator(EntityManager em) {
	this.em = em;
    }

    public Optional<Refueling> getFillUpBefore(final Date date) {
	final List<Refueling> refuelings = this.em
		.createNamedQuery(Refueling.FIND_BY_FILLED_UP_AND_DATE_BEFORE, Refueling.class)
		.setParameter("right", date, TemporalType.TIMESTAMP).getResultList();
	Optional<Refueling> result = Optional.absent();
	if (refuelings.size() > 0) {
	    result = Optional.of(refuelings.get(0));
	}
	return result;
    }

    public List<Refueling> getPartialRefuelingsBetween(final Date left, final Date right, final Vehicle vehicle) {
	final List<Refueling> result = this.em
		.createNamedQuery(Refueling.FIND_PARTIALS_BY_VEHICLE_AND_DATE_BETWEEN, Refueling.class)
		.setParameter("left", left).setParameter("right", right).setParameter("vehicle", vehicle)
		.getResultList();
	return result;
    }

}
