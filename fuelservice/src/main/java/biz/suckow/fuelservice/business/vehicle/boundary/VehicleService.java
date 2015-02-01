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

import biz.suckow.fuelservice.business.owner.boundary.OwnerService;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

@Stateless
public class VehicleService {

    @Inject
    private OwnerService ownerService;

    @Inject
    private EntityManager em;

    @Inject
    private Logger logger;

    public void persist(Vehicle vehicle) {
        this.em.persist(vehicle);
    }

    public Set<String> getNamesOfOwnedVehicles(String name) {
        Set<String> result = new HashSet<>();
        result.add("duke-car");
        return result;
    }

    public void addVehicle(String email, String vehicleName) {
        Optional<Owner> possibleOwner = this.ownerService.locateByEmail(email);
        if (possibleOwner.isPresent()) {
            Owner owner = possibleOwner.get();

            Vehicle vehicle = new Vehicle().setOwner(owner).setVehicleName(vehicleName);
            owner.addVehicle(vehicle);
            this.em.persist(vehicle);

            this.em.merge(owner);
        } else {
            throw new IllegalArgumentException("NO SUCH OWNER.");
        }
    }
}
