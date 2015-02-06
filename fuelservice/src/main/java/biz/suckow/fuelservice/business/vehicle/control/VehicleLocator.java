package biz.suckow.fuelservice.business.vehicle.control;

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

import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.*;

public class VehicleLocator {
    private final EntityManager em;

    @Inject
    public VehicleLocator(final EntityManager em) {
        this.em = em;
    }

    public Optional<Vehicle> getVehicle(final String email, final String vehicleName) {
        Vehicle result = null;
        try {
            result = this.em.createNamedQuery(Vehicle.QueryByEmailAndVehicleName.NAME, Vehicle.class)
                    .setParameter(Vehicle.QueryByEmailAndVehicleName.EMAIL, email)
                    .setParameter(Vehicle.QueryByEmailAndVehicleName.VEHICLE_NAME, vehicleName)
                    .getSingleResult();
        } catch (final NoResultException e) {
            /* NOP */
        }
        return Optional.ofNullable(result);
    }

    public Set<Vehicle> getVehicles(final String email) {
        List<Vehicle> result = new ArrayList<>();
        try {
            result = this.em.createNamedQuery(Vehicle.QueryByEmail.NAME, Vehicle.class)
                    .setParameter(Vehicle.QueryByEmail.EMAIL, email)
                    .getResultList();
        } catch (final NoResultException e) {
            /* NOP */
        }
        return new HashSet<>(result);
    }
}
