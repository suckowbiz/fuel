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
package biz.suckow.fuel.business.consumption.boundary;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.consumption.control.FuelConsumptionCalculator;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.boundary.RefuelingLocator;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

@Startup
@Singleton
public class FuelConsumptionScheduler {
    @Inject
    private RefuelingLocator locator;

    @Inject
    private FuelConsumptionCalculator maths;

    @Inject
    private Logger logger;

    @Inject
    private EntityManager em;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    // TODO test
    @Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
    public void timer() {
        if (this.isRunning.get()) {
            this.logger.log(Level.INFO, "Timer invocation blocked - still running.");
            return;
        }
        this.isRunning.set(true);
        List<Refueling> refuelings = Collections.emptyList();
        try {
            refuelings = this.locator.getFilledUpAndMissingConsumptionOldestFirst();
            for (Refueling refueling : refuelings) {
                final Optional<BigDecimal> possibleResult = this.maths.computeConsumptionFor(refueling);
                if (possibleResult.isPresent()) {
                    final FuelConsumption consumption = new FuelConsumption();
                    consumption.setDateComputed(refueling.getDateRefueled());
                    consumption.setLitresPerKilometre(possibleResult.get().doubleValue());
                    this.em.persist(consumption);

                    refueling.setConsumption(consumption);
                    refueling = this.em.merge(refueling);

                    final Vehicle vehicle = refueling.getVehicle();
                    vehicle.addFuelConsuption(consumption);
                    this.em.merge(vehicle);
                }
            }
        } catch (final Exception e) {
            this.logger.log(Level.SEVERE, "Fuel consumption timer crashed.", e);
        } finally {
            this.logger.log(Level.INFO, "Fuel consumption timer completed for {0} refuelings.", refuelings);
            this.isRunning.set(false);
        }
    }
}
