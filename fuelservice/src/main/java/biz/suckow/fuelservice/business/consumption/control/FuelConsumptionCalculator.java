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
    private final FuelStockLocator fuelStockLocator;

    private final Logger logger;

    private RefuellingStore refuellingStore;

    @Inject
    public FuelConsumptionCalculator(final RefuellingStore refuellingStore, final FuelStockLocator fuelStockLocator,
            final Logger logger) {
        this.refuellingStore = refuellingStore;
        this.fuelStockLocator = fuelStockLocator;
        this.logger = logger;
    }

    public Optional<BigDecimal> computeConsumption(final Refuelling refuelling) {
        if (refuelling.getIsFillUp() == false) {
            throw new IllegalArgumentException("Refuelling must not be a partial one.");
        }

        BigDecimal result = null;
        final Date refuelingDate = refuelling.getDateRefuelled();
        final Optional<Refuelling> possibleLastFillUp = this.refuellingStore.getFillUpBefore(refuelingDate);
        if (possibleLastFillUp.isPresent()) {
            final Vehicle vehicle = refuelling.getVehicle();
            final Date lastFillUpDate = possibleLastFillUp.get().getDateRefuelled();

            final BigDecimal litresConsumed = BigDecimal.valueOf(refuelling.getLitres())
                    .add(this.getLitresConsumedFromStock(lastFillUpDate, refuelingDate, vehicle))
                    .add(this.getLitresRefueledBetween(lastFillUpDate, refuelingDate, vehicle));

            final Long distanceDriven = refuelling.getKilometre() - possibleLastFillUp.get().getKilometre();
            if (distanceDriven == 0D) {
                this.logger.warning("Cannot compute consumption because distance evaluates to zero!");
            } else {
                result = litresConsumed.divide(BigDecimal.valueOf(distanceDriven), 4, RoundingMode.HALF_UP);
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
            // invert sign to fix if more consumed than added
            result = result.multiply(BigDecimal.valueOf(-1));
        }
        return result;
    }

    private BigDecimal sumStockReleaseLitres(final List<StockRelease> releases) {
        Double result = releases.stream().mapToDouble(StockRelease::getLitres).sum();
        return BigDecimal.valueOf(result);
    }

    private BigDecimal sumRefueledLitres(final List<Refuelling> refuellings) {
        Double result = refuellings.stream().mapToDouble(Refuelling::getLitres).sum();
        return BigDecimal.valueOf(result);
    }

    private BigDecimal sumStockAdditionsLitres(final List<StockAddition> additions) {
        Double result = additions.stream().mapToDouble(StockAddition::getLitres).sum();
        return BigDecimal.valueOf(result);
    }
}
