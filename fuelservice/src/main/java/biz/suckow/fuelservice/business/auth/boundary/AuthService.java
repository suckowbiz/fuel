package biz.suckow.fuelservice.business.auth.boundary;

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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;

@Stateless
public class AuthService {
    @Inject
    private EntityManager em;

    @Inject
    private OwnerStore ownerStore;

    public Optional<Owner> login(final String email, final String password) {
        Owner result = null;
        final Optional<Owner> possibleOwner = this.ownerStore.getByEmail(email);
        possibleOwner.orElseThrow(() -> new IllegalArgumentException("No such owner!"));
        final Owner owner = possibleOwner.get();
        if (owner.getPassword().equals(password)) {
            owner.setIsLoggedOut(false);
            result = this.em.merge(owner);
        }
        return Optional.ofNullable(result);
    }

    public void logout(final String email) {
        final Optional<Owner> possibleOwner = this.ownerStore.getByEmail(email);
        possibleOwner.orElseThrow(() -> new IllegalArgumentException("No such user!"));
        final Owner owner = possibleOwner.get();
        owner.setIsLoggedOut(true);
        this.em.merge(owner);
    }
}