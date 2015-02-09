package biz.suckow.fuelservice.business.owner.boundary;

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

import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.owner.entity.Role;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Stateless
public class OwnerStore {
    @Inject
    EntityManager em;

    public void create(String email, String password) {
        Owner owner = new Owner().addRole(Role.OWNER).setEmail(email).setPassword(password);
        this.em.persist(owner);
    }

    public Optional<Owner> getByEmail(final String email) {
        Owner result = null;
        try {
            result = (Owner) this.em.createNamedQuery(Owner.QueryByEmailCaseIgnore.NAME)
                    .setParameter(Owner.QueryByEmailCaseIgnore.EMAIL, email).getSingleResult();
        } catch (final NoResultException e) {
        /* NOP */
        }
        return Optional.ofNullable(result);
    }

    public OwnerPrincipal createOwnerPrincipal(String email) {
        OwnerPrincipal result = new OwnerPrincipal();
        Optional<Owner> possibleOwner = this.getByEmail(email);
        if (possibleOwner.isPresent()) {
            Set<String> vehicleNames = possibleOwner
                    .get()
                    .getVehicles()
                    .stream()
                    .map(Vehicle::getVehicleName)
                    .collect(Collectors.toSet());
            Set<Role> roles = possibleOwner
                    .get()
                    .getRoles()
                    .stream()
                    .collect(Collectors.toSet());
            boolean isLoggedOut = possibleOwner.get().getIsLoggedOut();
            result = new OwnerPrincipal().setName(email).setOwnedVehicleNames(vehicleNames).setRoles(roles).setLoggedOut(isLoggedOut);
        }
        return result;
    }

    public void removeByEmail(String email) {
        Optional<Owner> possibleOwner = this.getByEmail(email);
        possibleOwner.orElseThrow(() -> new IllegalArgumentException("No such owner!"));
        this.em.remove(possibleOwner.get());
    }
}
