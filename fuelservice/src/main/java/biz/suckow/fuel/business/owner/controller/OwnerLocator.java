package biz.suckow.fuel.business.owner.controller;

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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import biz.suckow.fuel.business.owner.entity.Owner;

import com.google.common.base.Optional;

@Stateless
public class OwnerLocator {
    @Inject
    private EntityManager em;

    public Optional<Owner> getOwner(final String ownername) {
        Owner result = null;
        try {
            result = (Owner) this.em.createNamedQuery(Owner.QueryByOwnernameIgnoreCase.NAME)
                    .setParameter(Owner.QueryByOwnernameIgnoreCase.OWNERNAME, ownername)
                    .getSingleResult();
        } catch (final NoResultException e) {
            /* NOP */
        }
        return Optional.fromNullable(result);
    }

}
