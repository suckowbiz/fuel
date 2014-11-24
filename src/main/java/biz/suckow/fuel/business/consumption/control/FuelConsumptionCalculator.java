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

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Inject;

import biz.suckow.fuel.business.refueling.boundary.RefuelingLocator;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class FuelConsumptionCalculator {
    private final RefuelingLocator refuelingLocator;
    private final FuelStockLocator fuelStockLocator;
    private final Logger logger;

    @Inject
    public FuelConsumptionCalculator(final RefuelingLocator refuelingLocator, final FuelStockLocator fuelStockLocator,
            final Logger logger) {
        this.refuelingLocator = refuelingLocator;
        this.fuelStockLocator = fuelStockLocator;
        this.logger = logger;
    }

    public Optional<BigDecimal> computeConsumptionFor(final Refueling refueling) {
        Preconditions.checkArgument(refueling.getIsFillUp());

        BigDecimal result = null;
        final Date refuelingDate = refueling.getDateRefueled();
        final Optional<Refueling> possibleLastFillUp = this.refuelingLocator.getFillUpBefore(refuelingDate);
        if (possibleLastFillUp.isPresent()) {
            final Vehicle vehicle = refueling.getVehicle();
            final Date lastFillUpDate = possibleLastFillUp.get().getDateRefueled();

            BigDecimal litres = BigDecimal.valueOf(refueling.getLitres());
            litres = litres.add(this.getLitresConsumedFromStock(lastFillUpDate, refuelingDate, vehicle));
            litres = litres.add(this.getLitresRefueledBetween(lastFillUpDate, refuelingDate, vehicle));

            final Double distance = refueling.getKilometre() - possibleLastFillUp.get().getKilometre();
            if (distance == 0D) {
                this.logger.warning("Cannot compute consumption because distance evaluates to zero!");
            } else {
                result = litres.divide(BigDecimal.valueOf(distance));
            }
        }
        return Optional.fromNullable(result);
    }

    private BigDecimal getLitresRefueledBetween(final Date left, final Date right, final Vehicle vehicle) {
        final List<Refueling> partials = this.refuelingLocator.getPartialRefuelingsBetween(left, right, vehicle);
        final BigDecimal result = this.sumRefueledLitres(partials);
        return result;
    }

    private BigDecimal getLitresConsumedFromStock(final Date left, final Date right, final Vehicle vehicle) {
        final List<StockAddition> stockAdditions = this.fuelStockLocator.getAdditionsBetween(left, right, vehicle);
        final BigDecimal litresAddedToStock = this.sumStockAdditionsLitres(stockAdditions);

        final List<StockRelease> stockReleases = this.fuelStockLocator.getReleasesBetween(left, right, vehicle);
        final BigDecimal litresReleasedFromStock = this.sumStockReleaseLitres(stockReleases);

        BigDecimal result = litresAddedToStock.subtract(litresReleasedFromStock);
        if (result.longValue() < 0L) {
            result = result.multiply(BigDecimal.valueOf(-1)); // in case more consumed than added within this interval
        }
        return result;
    }

    private BigDecimal sumStockReleaseLitres(final List<StockRelease> releases) {
        Double result = 0D;
        for (final StockRelease release : releases) {
            result += release.getLitres();
        }
        return BigDecimal.valueOf(result);
    }

    private BigDecimal sumRefueledLitres(final List<Refueling> refuelings) {
        Double result = 0D;
        for (final Refueling refueling : refuelings) {
            result += refueling.getLitres();
        }
        return BigDecimal.valueOf(result);
    }

    private BigDecimal sumStockAdditionsLitres(final List<StockAddition> additions) {
        Double result = 0D;
        for (final StockAddition addition : additions) {
            result += addition.getLitres();
        }
        return BigDecimal.valueOf(result);
    }
}
