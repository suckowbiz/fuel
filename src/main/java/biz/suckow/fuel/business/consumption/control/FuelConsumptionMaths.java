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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.refueling.boundary.RefuelingLocator;
import biz.suckow.fuel.business.refueling.entity.Refueling;

import com.google.common.base.Optional;

public class FuelConsumptionMaths {
    @Inject
    private EntityManager em;

    @Inject
    private RefuelingLocator locator;

    public Double calculate(final Refueling refueling) {
        // get right date interval
        final Date rightBorder = refueling.getDateRefueled();

        // get left date interval (earliest refueling before the given that is not a full refueling)
        // might be null on first time

        // query by date with isFull (not by existing consumption)
        List<Refueling> partials = new ArrayList<>();
        final Optional<Refueling> possibleLeftBorder = this.locator
                .getLatestFilledUpBefore(refueling.getDateRefueled());
        if (possibleLeftBorder.isPresent()) {
            partials = this.locator.getPartialRefuelingsBetween(possibleLeftBorder.get().getDateRefueled(),
                    rightBorder, refueling.getVehicle());
        } else {
            partials = this.locator.getAllPartialRefuelingsUntil(rightBorder, refueling.getVehicle());
        }

        // get all partial refuelings within interval

        // figure out stock at time of right date interval
        // sum up all refuelings and what is left of stock
        // divide litres by kilometers from actual refueling minus kilometer
        // from left date refueling

        // TODO 3. Implement
        throw new UnsupportedOperationException();
    }
}
