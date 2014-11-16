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

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.refueling.entity.Refueling;

public class FuelConsumptionMaths {
    @PersistenceContext
    private EntityManager em;

    public Double calculate(final Refueling refueling) {
        // get right date interval
        final Date rightBorder = refueling.getDateRefueled();

        // get left date interval
        final Date leftBorder = (Date) this.em
                .createNamedQuery(Refueling.QueryByExistingConsumptionForDateNewestFirst.NAME)
                .setParameter(Refueling.QueryByExistingConsumptionForDateNewestFirst.PARAM_NAME,
                        refueling.getDateRefueled(), TemporalType.TIMESTAMP)
                .getResultList();

        // get all partial refuelings within interval
        // figure out stock at time of right date interval
        // sum up all refuelings and what is left of stock
        // divide litres by kilometers from actual refueling minus kilometer
        // from left date refueling

        // TODO 3. Implement
        throw new UnsupportedOperationException();
    }
}
