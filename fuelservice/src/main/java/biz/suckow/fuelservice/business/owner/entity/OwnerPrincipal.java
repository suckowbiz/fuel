package biz.suckow.fuelservice.business.owner.entity;

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

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

public class OwnerPrincipal implements Principal {
    private String name;
    private boolean isLoggedOut = true;
    private Set<Role> roles = new HashSet<>();
    private Set<String> ownedVehicleNames = new HashSet<>();

    public boolean isLoggedOut() {
        return isLoggedOut;
    }

    public OwnerPrincipal setLoggedOut(boolean isLoggedOut) {
        this.isLoggedOut = isLoggedOut;
        return this;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public OwnerPrincipal setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Set<String> getOwnedVehicleNames() {
        return ownedVehicleNames;
    }

    public OwnerPrincipal setOwnedVehicleNames(Set<String> ownedVehicleNames) {
        this.ownedVehicleNames = ownedVehicleNames;
        return this;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public OwnerPrincipal setName(String name) {
        this.name = name;
        return this;
    }
}
