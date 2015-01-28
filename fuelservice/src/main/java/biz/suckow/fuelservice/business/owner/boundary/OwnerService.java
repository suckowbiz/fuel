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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Optional;

@Stateless
public class OwnerService {
    @Inject
    private OwnerLocator locator;

    @Inject
    private EntityManager em;

    public void create(String email, String password) {
        Owner owner = new Owner().setEmail(email).setPassword(password);
        this.em.persist(owner);
    }

    public Optional<Owner> locateByEmail(final String email) {
        final Optional<Owner> result = this.locator.getOwner(email);
        return result;
    }
}
