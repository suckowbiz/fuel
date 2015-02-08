package biz.suckow.fuelservice.business.consumption.control;

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

import biz.suckow.fuelservice.business.refuelling.boundary.RefuellingStore;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.StockAddition;
import biz.suckow.fuelservice.business.refuelling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.inject.Inject;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class FuelConsumptionCalculator {
    private RefuellingStore refuellingStore;
    private final FuelStockLocator fuelStockLocator;
    private final Logger logger;

    @Inject
    public FuelConsumptionCalculator(final RefuellingStore refuellingStore, final FuelStockLocator fuelStockLocator,
                                     final Logger logger) {
        this.refuellingStore = refuellingStore;
        this.fuelStockLocator = fuelStockLocator;
        this.logger = logger;
    }

    public java.util.Optional<BigDecimal> computeConsumptionFor(final Refuelling refuelling) {
        if (refuelling.getIsFillUp() == false) {
            throw new IllegalArgumentException("Refuelling must not be a partial one.");
        }

        BigDecimal result = null;
        final Date refuelingDate = refuelling.getDateRefuelled();
        final Optional<Refuelling> possibleLastFillUp = this.refuellingStore.getFillUpBefore(refuelingDate);
        if (possibleLastFillUp.isPresent()) {
            final Vehicle vehicle = refuelling.getVehicle();
            final Date lastFillUpDate = possibleLastFillUp.get().getDateRefuelled();

            BigDecimal litres = BigDecimal.valueOf(refuelling.getLitres());
            litres = litres.add(this.getLitresConsumedFromStock(lastFillUpDate, refuelingDate, vehicle));
            litres = litres.add(this.getLitresRefueledBetween(lastFillUpDate, refuelingDate, vehicle));

            final Long distance = refuelling.getKilometre() - possibleLastFillUp.get().getKilometre();
            if (distance == 0D) {
                this.logger.warning("Cannot compute consumption because distance evaluates to zero!");
            } else {
                result = litres.divide(BigDecimal.valueOf(distance), 4, RoundingMode.HALF_UP);
            }
        }
        return Optional.ofNullable(result);
    }

    private BigDecimal getLitresRefueledBetween(final Date left, final Date right, final Vehicle vehicle) {
        final List<Refuelling> partials = this.refuellingStore.getPartialRefuellingsBetween(left, right, vehicle);
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
            result = result.multiply(BigDecimal.valueOf(-1)); // in case more
            // consumed than
            // added within
            // this interval
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

    private BigDecimal sumRefueledLitres(final List<Refuelling> refuellings) {
        Double result = 0D;
        for (final Refuelling refuelling : refuellings) {
            result += refuelling.getLitres();
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
