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

import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.refuelling.boundary.RefuellingStore;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.StockAddition;
import biz.suckow.fuelservice.business.refuelling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

public class FuelConsumptionCalculatorTest extends EasyMockSupport {
    @Mock
    private RefuellingStore refuellingStoreMock;

    @Mock
    private FuelStockLocator fuelStockLocatorMock;

    @Mock(type = MockType.NICE)
    private EntityManager emMock;

    @BeforeClass
    public void BeforeClass() {
        injectMocks(this);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nonFillUpMustFail() {
        final Refuelling refuelling = new Refuelling.Builder().kilometre(100L).litres(10D).dateRefueled(new Date())
                .fillUp(false).build();
        new FuelConsumptionCalculator(this.refuellingStoreMock, this.fuelStockLocatorMock, Logger.getAnonymousLogger())
                .computeConsumption(refuelling);
    }

    @Test
    public void mustNotComputeWithoutPredecessor() {
        this.resetAll();
        expect(this.refuellingStoreMock.getFillUpBefore(anyObject(Date.class))).andStubReturn(
                Optional.<Refuelling>empty());
        this.replayAll();

        final Refuelling refuelling = new Refuelling.Builder().kilometre(100L).litres(10D).dateRefueled(new Date())
                .fillUp(true).build();

        final Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuellingStoreMock,
                this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumption(refuelling);
        assertThat(actualResult.isPresent()).isFalse();
    }

    @Test(description = "Simple case of refuelling and consuming this refuelling.")
    public void simpleComputationMustSucceed() {
        final Date january = TestHelper.getMonth(0);
        final Date february = TestHelper.getMonth(1);

        final Owner duke = TestHelper.createDuke();
        final Vehicle vehicle = TestHelper.createDukeCar(duke);

        final Refuelling refuellingBefore = new Refuelling.Builder().kilometre(100L).litres(10D).dateRefueled(january)
                .fillUp(true).build();
        final Refuelling refuelling = new Refuelling.Builder().kilometre(200L).litres(10D).dateRefueled(february)
                .fillUp(true).vehicle(vehicle).build();

        this.resetAll();
        expect(this.refuellingStoreMock.getFillUpBefore(february)).andStubReturn(Optional.of(refuellingBefore));
        expect(this.fuelStockLocatorMock.getAdditionsBetween(january, february, vehicle)).andStubReturn(
                Collections.emptyList());
        expect(this.refuellingStoreMock.getPartialRefuellingsBetween(january, february, vehicle)).andStubReturn(
                Collections.emptyList());
        expect(this.fuelStockLocatorMock.getReleasesBetween(january, february, vehicle)).andStubReturn(
                Collections.emptyList());
        this.replayAll();

        final Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuellingStoreMock,
                this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumption(refuelling);
        assertThat(actualResult.isPresent());
        assertThat(actualResult.get().doubleValue()).isEqualTo(10D / 100D);
        this.verifyAll();
    }

    @SuppressWarnings("serial")
    @Test(description = "Have a refuelling with consideration of stock and consumption of this refuelling and stock.")
    public void complexRefuelingMustSucceed() {
        final Date january = TestHelper.getMonth(0);
        final Date february = TestHelper.getMonth(1);
        final Date march = TestHelper.getMonth(2);
        final Date april = TestHelper.getMonth(3);

        final Owner duke = TestHelper.createDuke();
        final Vehicle vehicle = TestHelper.createDukeCar(duke);

        final double litresPartiallyRefueled = 20D;
        final double litresFilledUp = 40D;
        final double litresToStock = 5D;
        final double litresFromStock = 5D;
        final long kilometresLastFillUp = 100L;
        final long kilometresFilledUp = 1100L;
        final double expectedConsumption = (litresFilledUp + litresPartiallyRefueled + litresToStock - litresFromStock)
                / (kilometresFilledUp - kilometresLastFillUp);

        final Refuelling partialRefuelling = new Refuelling.Builder().litres(litresPartiallyRefueled)
                .dateRefueled(march)
                .fillUp(false).build();
        final Refuelling refuellingBefore = new Refuelling.Builder().kilometre(kilometresLastFillUp)
                .litres(litresFilledUp).dateRefueled(january).fillUp(true).build();
        final Refuelling refuelling = new Refuelling.Builder().kilometre(kilometresFilledUp).litres(litresFilledUp)
                .dateRefueled(april).fillUp(true).vehicle(vehicle).build();
        final StockAddition addition = new StockAddition().setDateAdded(february).setLitres(litresToStock);
        final StockRelease release = new StockRelease().setDateReleased(march).setLitres(litresFromStock);

        this.resetAll();
        expect(this.refuellingStoreMock.getFillUpBefore(april)).andStubReturn(Optional.of(refuellingBefore));
        expect(this.fuelStockLocatorMock.getAdditionsBetween(january, april, vehicle)).andStubReturn(
                Arrays.asList(addition));
        expect(this.refuellingStoreMock.getPartialRefuellingsBetween(january, april, vehicle)).andStubReturn(
                Arrays.asList(partialRefuelling));
        expect(this.fuelStockLocatorMock.getReleasesBetween(january, april, vehicle)).andStubReturn(
                Arrays.asList(release));
        this.replayAll();

        final Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuellingStoreMock,
                this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumption(refuelling);
        assertThat(actualResult.isPresent());
        assertThat(actualResult.get().doubleValue()).isEqualTo(expectedConsumption);
        this.verifyAll();
    }
}
