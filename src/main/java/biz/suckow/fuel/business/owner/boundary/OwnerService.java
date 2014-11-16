/*
 * Copyright 2014 Tobias Suckow.
 *
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
 */
package biz.suckow.fuel.business.owner.boundary;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.owner.entity.Owner;

@Stateless
public class OwnerService extends BaseEntity {
    /**
     *
     */
    private static final long serialVersionUID = -218334641369264690L;
    @PersistenceContext
    private EntityManager em;

    // TODO write test
    public Owner getOwner(final String ownername) {
        final Owner result = (Owner) this.em
                .createNamedQuery(Owner.QueryByOwnerameCaseIgnore.NAME)
                .setParameter(Owner.QueryByOwnerameCaseIgnore.PARAM_NAME, ownername)
                .getSingleResult();
        return result;
    }

}
