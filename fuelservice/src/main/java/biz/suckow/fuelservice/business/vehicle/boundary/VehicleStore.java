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

    public void persist(final Vehicle vehicle) {
        this.em.persist(vehicle);
    }

    public void removeOwnersVehicle(final String vehicleName, final String ownerEmail) {
        final Optional<Owner> possibleOwner = this.ownerStore.getByEmail(ownerEmail);
        possibleOwner.orElseThrow(() -> new IllegalArgumentException("No such user!"));
        boolean needleFound = false;
        final Owner owner = possibleOwner.get();
        for (final Vehicle vehicle : owner.getVehicles()) {
            if (vehicle.getVehicleName().equals(vehicleName)) {
                needleFound = true;
                owner.getVehicles().remove(vehicle);
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

    public void persistNewVehicle(final String email, final String vehicleName) {
        final Optional<Owner> possibleOwner = this.ownerStore.getByEmail(email);
        if (possibleOwner.isPresent()) {
            final Owner owner = possibleOwner.get();

            Vehicle vehicle = new Vehicle().setOwner(owner).setVehicleName(vehicleName);
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
            result = this.em.createNamedQuery(Vehicle.BY_EMAIL_AND_VEHICLE_NAME, Vehicle.class)
                    .setParameter("email", email)
                    .setParameter("vehicleName", vehicleName)
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
            result = this.em.createNamedQuery(Vehicle.BY_EMAIL, Vehicle.class)
                    .setParameter("email", email)
                    .getResultList();
        }
        catch (final NoResultException e) {
            /* NOP */
        }
        return new HashSet<>(result);
    }
}
