package biz.suckow.fuel.business.consumption.control;

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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.expect;

import java.math.BigDecimal;
import java.util.Date;
import java.util.logging.Logger;

import javax.persistence.EntityManager;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.MockType;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.TestHelper;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.control.RefuelingLocator;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

public class FuelConsumptionCalculatorTest extends EasyMockSupport {
    @Mock
    RefuelingLocator refuelingLocatorMock;

    @Mock
    FuelStockLocator fuelStockLocatorMock;

    @Mock(type = MockType.NICE)
    EntityManager emMock;

    @BeforeClass
    public void BeforeClass() {
	injectMocks(this);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void nonFillUpMustFail() {
	Refueling refueling = new Refueling.Builder().kilometre(100D).litres(10D).dateRefueled(new Date())
		.fillUp(false).build();
	new FuelConsumptionCalculator(this.refuelingLocatorMock, this.fuelStockLocatorMock, Logger.getAnonymousLogger())
		.computeConsumptionFor(refueling);
    }

    @Test
    public void mustNotComputeWithoutPredecessor() {
	this.resetAll();
	expect(this.refuelingLocatorMock.getFillUpBefore(anyObject(Date.class))).andStubReturn(
		Optional.<Refueling> absent());
	this.replayAll();

	Refueling refueling = new Refueling.Builder().kilometre(100D).litres(10D).dateRefueled(new Date()).fillUp(true)
		.build();

	Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuelingLocatorMock,
		this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumptionFor(refueling);
	assertThat(actualResult).isAbsent();
    }

    @Test
    public void simpleComputationMustSucceed() {
	Date january = TestHelper.getMonth(0);
	Date february = TestHelper.getMonth(1);
	
	Owner duke = TestHelper.createDuke();
	Vehicle vehicle = TestHelper.createDukeCar(duke);
	
	Refueling refuelingBefore = new Refueling.Builder().kilometre(100D).litres(10D).dateRefueled(january)
		.fillUp(true).build();
	Refueling refueling = new Refueling.Builder().kilometre(200D).litres(10D).dateRefueled(february).fillUp(true)
		.vehicle(vehicle).build();

	this.resetAll();
	expect(this.refuelingLocatorMock.getFillUpBefore(february)).andStubReturn(Optional.of(refuelingBefore));
	expect(this.fuelStockLocatorMock.getAdditionsBetween(january, february, vehicle)).andStubReturn(
		Lists.<StockAddition> newArrayList());
	expect(this.refuelingLocatorMock.getPartialRefuelingsBetween(january, february, vehicle)).andStubReturn(
		Lists.<Refueling> newArrayList());
	expect(this.fuelStockLocatorMock.getReleasesBetween(january, february, vehicle)).andStubReturn(
		Lists.<StockRelease> newArrayList());
	this.replayAll();

	Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuelingLocatorMock,
		this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumptionFor(refueling);
	assertThat(actualResult).isPresent();
	assertThat(actualResult.get().doubleValue()).isEqualTo(10D / 100D);
    }
    //
    // public Optional<Double> computeConsumptionFor( Refueling refueling) {
    // Preconditions.checkArgument(refueling.getIsFillUp());
    //
    // Double result = null;
    // Date refuelingDate = refueling.getDateRefueled();
    // Optional<Refueling> possibleLastFillUp =
    // this.refuelingLocator.getFillUpBefore(refuelingDate);
    // if (possibleLastFillUp.isPresent()) {
    // Vehicle vehicle = refueling.getVehicle();
    // Date lastFillUpDate = possibleLastFillUp.get().getDateRefueled();
    //
    // Double litres = refueling.getLitres();
    // litres += this.getLitresConsumedFromStock(lastFillUpDate, refuelingDate,
    // vehicle);
    // litres += this.getLitresRefueledBetween(lastFillUpDate, refuelingDate,
    // vehicle);
    //
    // Double distance = refueling.getKilometre() -
    // possibleLastFillUp.get().getKilometre();
    // result = litres / distance;
    // }
    // return Optional.fromNullable(result);
    // }
    //
    // private Double getLitresRefueledBetween( Date left, Date right, Vehicle
    // vehicle) {
    // List<Refueling> partials =
    // this.refuelingLocator.getPartialRefuelingsBetween(left, right, vehicle);
    // Double result = this.sumRefueledLitres(partials);
    // return result;
    // }
    //
    // private Double getLitresConsumedFromStock( Date left, Date right, Vehicle
    // vehicle) {
    // List<StockAddition> stockAdditions =
    // this.fuelStockLocator.getAdditionsBetween(left, right, vehicle);
    // Double litresAddedToStock = this.sumStockAdditionsLitres(stockAdditions);
    //
    // List<StockRelease> stockReleases =
    // this.fuelStockLocator.getReleasesBetween(left, right, vehicle);
    // Double litresReleasedFromStock =
    // this.sumStockReleaseLitres(stockReleases);
    //
    // Double result = litresAddedToStock - litresReleasedFromStock;
    // if (result < 0) {
    // result *= -1; // in case more consumed than added within this interval
    // }
    // return result;
    // }
    //
    // private Double sumStockReleaseLitres( List<StockRelease> releases) {
    // Double result = 0D;
    // for ( StockRelease release : releases) {
    // result += release.getLitres();
    // }
    // return result;
    // }
    //
    // private Double sumRefueledLitres( List<Refueling> refuelings) {
    // Double result = 0D;
    // for ( Refueling refueling : refuelings) {
    // result += refueling.getLitres();
    // }
    // return result;
    // }
    //
    // private Double sumStockAdditionsLitres( List<StockAddition> additions) {
    // Double result = 0D;
    // for ( StockAddition addition : additions) {
    // result += addition.getLitres();
    // }
    // return result;
    // }
}
