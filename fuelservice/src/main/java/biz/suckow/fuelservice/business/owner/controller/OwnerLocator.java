package biz.suckow.fuelservice.business.owner.controller;

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

import biz.suckow.fuelservice.business.owner.entity.Owner;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.Optional;

public class OwnerLocator {
    private final EntityManager em;

    @Inject
    public OwnerLocator(final EntityManager em) {
        this.em = em;
    }

    public Optional<Owner> getOwner(final String email) {
        Owner result = null;
        try {
            result = (Owner) this.em.createNamedQuery(Owner.QueryByEmailCaseIgnore.NAME)
                    .setParameter(Owner.QueryByEmailCaseIgnore.EMAIL, email).getSingleResult();
        } catch (final NoResultException e) {
        /* NOP */
        }
        return Optional.ofNullable(result);
    }

}
