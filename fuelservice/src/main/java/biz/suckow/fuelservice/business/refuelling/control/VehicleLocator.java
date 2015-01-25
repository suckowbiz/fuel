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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class VehicleLocator {
    private final EntityManager em;

    @Inject
    public VehicleLocator(final EntityManager em) {
	this.em = em;
    }

    public Optional<Vehicle> getVehicle(final String ownername, final String vehiclename) {
	Vehicle result = null;
	try {
	    result = this.em.createNamedQuery(Vehicle.FIND_BY_OWNERS_AND_VEHICLES_NAME, Vehicle.class)
		    .setParameter("ownerName", ownername).setParameter("vehicleName", vehiclename).getSingleResult();
	} catch (final NoResultException e) {
	    /* NOP */
	}
	return Optional.ofNullable(result);
    }
}