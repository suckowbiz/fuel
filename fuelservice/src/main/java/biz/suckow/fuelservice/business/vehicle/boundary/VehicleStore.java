package biz.suckow.fuelservice.business.vehicle.boundary;

/*
 * #%L
 * fuelservice
 * %%
 * Copyright (C) 2014 - 2015 Suckow.biz
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

import biz.suckow.fuelservice.business.owner.boundary.OwnerStore;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.*;
import java.util.logging.Logger;

@Stateless
public class VehicleStore {

    @Inject
    OwnerStore ownerStore;

    @Inject
    EntityManager em;

    @Inject
    Logger logger;

    public void persist(Vehicle vehicle) {
        this.em.persist(vehicle);
    }

    public void removeOwnersVehicle(String vehicleName, String ownerEmail) {
        Optional<Owner> possibleOwner = this.ownerStore.getByEmail(ownerEmail);
        possibleOwner.orElseThrow(() -> new IllegalArgumentException("No such user!"));
        boolean needleFound = false;
        Owner owner = possibleOwner.get();
        for (Vehicle vehicle : owner.getVehicles()) {
            if (vehicle.getVehicleName()
                       .equals(vehicleName)) {
                needleFound = true;
                owner.getVehicles()
                     .remove(vehicle);
                this.em.merge(owner);

                // remove cascades
                this.em.remove(vehicle);
                break;
            }
        }
        if (needleFound == false) {
            throw new IllegalArgumentException("No such vehicle!");
        }
    }

    public void persistNewVehicle(String email, String vehicleName) {
        Optional<Owner> possibleOwner = this.ownerStore.getByEmail(email);
        if (possibleOwner.isPresent()) {
            Owner owner = possibleOwner.get();

            Vehicle vehicle = new Vehicle().setOwner(owner)
                                           .setVehicleName(vehicleName);
            this.em.persist(vehicle);

            owner.addVehicle(vehicle);
            this.em.merge(owner);
        } else {
            throw new IllegalArgumentException("NO SUCH OWNER.");
        }
    }

    public Optional<Vehicle> getVehicleByNameAndOwnerEmail(final String email, final String vehicleName) {
        Vehicle result = null;
        try {
            result = this.em.createNamedQuery(Vehicle.QueryByEmailAndVehicleName.NAME, Vehicle.class)
                            .setParameter(Vehicle.QueryByEmailAndVehicleName.EMAIL, email)
                            .setParameter(Vehicle.QueryByEmailAndVehicleName.VEHICLE_NAME, vehicleName)
                            .getSingleResult();
        }
        catch (final NoResultException e) {
            /* NOP */
        }
        return Optional.ofNullable(result);
    }

    public Set<Vehicle> getVehiclesByOwnerEmail(final String email) {
        List<Vehicle> result = new ArrayList<>();
        try {
            result = this.em.createNamedQuery(Vehicle.QueryByEmail.NAME, Vehicle.class)
                            .setParameter(Vehicle.QueryByEmail.EMAIL, email)
                            .getResultList();
        }
        catch (final NoResultException e) {
            /* NOP */
        }
        return new HashSet<>(result);
    }
}
