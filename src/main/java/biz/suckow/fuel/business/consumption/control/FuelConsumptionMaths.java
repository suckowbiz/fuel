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
import java.util.List;

import javax.inject.Inject;

import biz.suckow.fuel.business.refueling.boundary.RefuelingLocator;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class FuelConsumptionMaths {
    @Inject
    private RefuelingLocator refuelingLocator;

    @Inject
    private FuelStockLocator fuelStockLocator;

    public Optional<Double> calculate(final Refueling refueling) {
        Double result = null;
        final Date refuelingDate = refueling.getDateRefueled();
        final Optional<Refueling> possibleLastFillUpRefueling = this.refuelingLocator.getLatestFilledUpBefore(refuelingDate);
        if (possibleLastFillUpRefueling.isPresent()) {
            final Refueling lastRefueling = possibleLastFillUpRefueling.get();

            final Double litresConsumed = this.getConsumption(refueling, lastRefueling.getDateRefueled(), refuelingDate);
            final Double litresConsumedFromStock = this.getStockConsumption(lastRefueling.getDateRefueled(),
                    refuelingDate, refueling.getVehicle());

            final double consumed = litresConsumed + litresConsumedFromStock;
            final Double distance = refueling.getKilometre() - lastRefueling.getKilometre();
            result = consumed / distance;
        }
        return Optional.fromNullable(result);
    }

    private Double getConsumption(final Refueling refueling, final Date lastRefuelingDate, final Date refuelingDate) {
        final List<Refueling> partials = this.refuelingLocator.getPartialRefuelingsBetween(lastRefuelingDate,
                refuelingDate, refueling.getVehicle());
        Double litresConsumed = this.sumRefuelingLitres(partials);
        litresConsumed += refueling.getLitres();
        return litresConsumed;
    }

    private Double getStockConsumption(final Date left, final Date right, final Vehicle vehicle) {
        final List<StockAddition> stockAdditions = this.fuelStockLocator.getAdditionsBetween(left, right, vehicle);
        final Double litresAddedToStock = this.sumStockAdditionsLitres(stockAdditions);

        final List<StockRelease> stockReleases = this.fuelStockLocator.getReleasesBetween(left, right, vehicle);
        final Double litresReleasedFromStock = this.sumStockReleaseLitres(stockReleases);

        Double result = litresAddedToStock - litresReleasedFromStock;
        if (result < 0) {
            result *= -1; // in case more consumed than added within this interval
        }
        return result;
    }

    private Double sumStockReleaseLitres(final List<StockRelease> releases) {
        Double result = 0D;
        for (final StockRelease release : releases) {
            result += release.getLitres();
        }
        return result;
    }

    private Double sumRefuelingLitres(final List<Refueling> refuelings) {
        Double result = 0D;
        for (final Refueling refueling : refuelings) {
            result += refueling.getLitres();
        }
        return result;
    }

    private Double sumStockAdditionsLitres(final List<StockAddition> additions) {
        Double result = 0D;
        for (final StockAddition addition : additions) {
            result += addition.getLitres();
        }
        return result;
    }
}
