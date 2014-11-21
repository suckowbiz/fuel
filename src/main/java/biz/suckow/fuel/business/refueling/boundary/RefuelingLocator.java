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
package biz.suckow.fuel.business.refueling.boundary;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class RefuelingLocator {
    @Inject
    private EntityManager em;

    public List<Refueling> getFilledUpAndMissingConsumptionOldestFirst() {
        final List<Refueling> result = this.em.createNamedQuery(
                Refueling.BY_FILLED_UP_AND_MISSING_CONSUMPTION_OLDEST_FIRST, Refueling.class).getResultList();
        return result;
    }

    public Optional<Refueling> getLatestFilledUpBefore(final Date date) {
        final List<Refueling> refuelings = this.em.createNamedQuery(Refueling.QueryLatestByFilledUpBeforeDate.NAME,
                Refueling.class)
                .setParameter(Refueling.QueryLatestByFilledUpBeforeDate.DATE, date, TemporalType.TIMESTAMP)
                .getResultList();
        Optional<Refueling> result = Optional.absent();
        if (refuelings.size() > 0) {
            result = Optional.of(refuelings.get(0));
        }
        return result;
    }

    public List<Refueling> getPartialRefuelingsBetween(final Date left, final Date right, final Vehicle vehicle) {
        final List<Refueling> result = this.em.createNamedQuery(Refueling.QueryPartialRefuelingsBetween.NAME,
                Refueling.class)
                .setParameter(Refueling.QueryPartialRefuelingsBetween.DATE_LEFT, left)
                .setParameter(Refueling.QueryPartialRefuelingsBetween.DATE_RIGHT, right)
                .setParameter(Refueling.QueryPartialRefuelingsBetween.VEHICLE, vehicle)
                .getResultList();
        return result;
    }

    /**
     * @param refuelingDate
     * @param vehicle
     * @return
     */
    public List<Refueling> getAllPartialRefuelingsUntil(final Date date, final Vehicle vehicle) {
        // TODO Auto-generated method stub
        return null;
    }

}
