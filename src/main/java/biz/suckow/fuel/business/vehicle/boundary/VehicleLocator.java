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
package biz.suckow.fuel.business.vehicle.boundary;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class VehicleLocator {
    @PersistenceContext
    private EntityManager em;

    public Optional<Vehicle> getVehicle(final String ownername, final String vehiclename) {
        Vehicle result = null;
        try {
            result = (Vehicle) this.em.createNamedQuery(Vehicle.QueryByOwnerAndVehicle.NAME)
                    .setParameter(Vehicle.QueryByOwnerAndVehicle.PARAM_OWNERNAME_NAME, ownername)
                    .setParameter(Vehicle.QueryByOwnerAndVehicle.PARAM_VEHICLENAME_NAME, vehiclename).getSingleResult();
        } catch (final NoResultException e) {
            /* NOP */
        }
        return Optional.fromNullable(result);
    }
}
