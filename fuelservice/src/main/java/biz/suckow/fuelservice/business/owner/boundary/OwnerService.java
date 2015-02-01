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

import biz.suckow.fuelservice.business.owner.controller.OwnerLocator;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.owner.entity.Role;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Stateless
public class OwnerService {
    @Inject
    private OwnerLocator locator;

    @Inject
    private EntityManager em;

    @Inject
    private Logger logger;

    public void create(String email, String password) {
        Owner owner = new Owner().addRole(Role.OWNER).setEmail(email).setPassword(password);
        this.em.persist(owner);
    }

    public Optional<Owner> locateByEmail(final String email) {
        final Optional<Owner> result = this.locator.getOwner(email);
        return result;
    }

    public Optional<OwnerPrincipal> createOwnerPrincipal(String email) {
        OwnerPrincipal result = null;
        Optional<Owner> possibleOwner = this.locateByEmail(email);
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
            result = new OwnerPrincipal().setName(email).setOwnedVehicleNames(vehicleNames).setRoles(roles);
        } else {
            throw new IllegalArgumentException("No sucher owner!");
        }
        return Optional.ofNullable(result);
    }
}
