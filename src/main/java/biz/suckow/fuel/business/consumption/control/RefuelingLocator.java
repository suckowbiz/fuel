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
package biz.suckow.fuel.business.consumption.control;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.refueling.entity.Refueling;

import com.google.common.base.Optional;

public class RefuelingLocator {
    @PersistenceContext
    private EntityManager em;

    public List<Refueling> getFilledUpRefuelingsMissingConsumptionOldestFirst() {
        @SuppressWarnings("unchecked")
        final List<Refueling> result = this.em.createNamedQuery(
                Refueling.BY_FILLED_UP_WITH_MISSING_CONSUMPTION_OLDEST_FIRST).getResultList();
        return result;
    }

    // TODO test
    public Optional<Refueling> get(final Refueling refueling) {
        @SuppressWarnings("unchecked")
        final List<Refueling> refuelings = this.em
                .createNamedQuery(
                        Refueling.QueryByExistingConsumptionForDateNewestFirst.NAME)
                .setParameter(
                        Refueling.QueryByExistingConsumptionForDateNewestFirst.PARAM_NAME,
                        refueling.getDateRefueled(), TemporalType.TIMESTAMP)
                .getResultList();
        Optional<Refueling> result = Optional.absent();
        if (refuelings.size() > 0) {
            result = Optional.of(refuelings.get(0));
        }
        return result;
    }
}
