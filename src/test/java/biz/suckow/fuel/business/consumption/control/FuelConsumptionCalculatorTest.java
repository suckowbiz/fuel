package biz.suckow.fuel.business.consumption.control;

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
        final Refueling refueling = new Refueling.Builder().kilometre(100D)
                .litres(10D)
                .dateRefueled(new Date())
                .fillUp(false)
                .build();
        new FuelConsumptionCalculator(this.refuelingLocatorMock, this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumptionFor(refueling);
    }

    @Test
    public void mustNotComputeWithoutPredecessor() {
        this.resetAll();
        expect(this.refuelingLocatorMock.getFillUpBefore(anyObject(Date.class))).andStubReturn(
                Optional.<Refueling> absent());
        this.replayAll();

        final Refueling refueling = new Refueling.Builder().kilometre(100D)
                .litres(10D)
                .dateRefueled(new Date())
                .fillUp(true)
                .build();

        final Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuelingLocatorMock,
                this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumptionFor(refueling);
        assertThat(actualResult).isAbsent();
    }

    @Test
    public void simpleComputationMustSucceed() {
        final Date january = TestHelper.getMonth(0);
        final Date february = TestHelper.getMonth(1);
        final Vehicle vehicle = TestHelper.getCreatedAndPersistedDukeCar(this.emMock);
        final Refueling refuelingBefore = new Refueling.Builder().kilometre(100D)
                .litres(10D)
                .dateRefueled(january)
                .fillUp(true)
                .build();
        final Refueling refueling = new Refueling.Builder().kilometre(200D)
                .litres(10D)
                .dateRefueled(february)
                .fillUp(true)
                .vehicle(vehicle)
                .build();

        this.resetAll();
        expect(this.refuelingLocatorMock.getFillUpBefore(february)).andStubReturn(Optional.of(refuelingBefore));
        expect(this.fuelStockLocatorMock.getAdditionsBetween(january, february, vehicle)).andStubReturn(
                Lists.<StockAddition> newArrayList());
        expect(this.refuelingLocatorMock.getPartialRefuelingsBetween(january, february, vehicle)).andStubReturn(
                Lists.<Refueling> newArrayList());
        expect(this.fuelStockLocatorMock.getReleasesBetween(january, february, vehicle)).andStubReturn(
                Lists.<StockRelease> newArrayList());
        this.replayAll();

        final Optional<BigDecimal> actualResult = new FuelConsumptionCalculator(this.refuelingLocatorMock,
                this.fuelStockLocatorMock, Logger.getAnonymousLogger()).computeConsumptionFor(refueling);
        assertThat(actualResult).isPresent();
        assertThat(actualResult.get().doubleValue()).isEqualTo(10D / 100D);
    }
    //
    // public Optional<Double> computeConsumptionFor(final Refueling refueling) {
    // Preconditions.checkArgument(refueling.getIsFillUp());
    //
    // Double result = null;
    // final Date refuelingDate = refueling.getDateRefueled();
    // final Optional<Refueling> possibleLastFillUp = this.refuelingLocator.getFillUpBefore(refuelingDate);
    // if (possibleLastFillUp.isPresent()) {
    // final Vehicle vehicle = refueling.getVehicle();
    // final Date lastFillUpDate = possibleLastFillUp.get().getDateRefueled();
    //
    // Double litres = refueling.getLitres();
    // litres += this.getLitresConsumedFromStock(lastFillUpDate, refuelingDate, vehicle);
    // litres += this.getLitresRefueledBetween(lastFillUpDate, refuelingDate, vehicle);
    //
    // final Double distance = refueling.getKilometre() - possibleLastFillUp.get().getKilometre();
    // result = litres / distance;
    // }
    // return Optional.fromNullable(result);
    // }
    //
    // private Double getLitresRefueledBetween(final Date left, final Date right, final Vehicle vehicle) {
    // final List<Refueling> partials = this.refuelingLocator.getPartialRefuelingsBetween(left, right, vehicle);
    // final Double result = this.sumRefueledLitres(partials);
    // return result;
    // }
    //
    // private Double getLitresConsumedFromStock(final Date left, final Date right, final Vehicle vehicle) {
    // final List<StockAddition> stockAdditions = this.fuelStockLocator.getAdditionsBetween(left, right, vehicle);
    // final Double litresAddedToStock = this.sumStockAdditionsLitres(stockAdditions);
    //
    // final List<StockRelease> stockReleases = this.fuelStockLocator.getReleasesBetween(left, right, vehicle);
    // final Double litresReleasedFromStock = this.sumStockReleaseLitres(stockReleases);
    //
    // Double result = litresAddedToStock - litresReleasedFromStock;
    // if (result < 0) {
    // result *= -1; // in case more consumed than added within this interval
    // }
    // return result;
    // }
    //
    // private Double sumStockReleaseLitres(final List<StockRelease> releases) {
    // Double result = 0D;
    // for (final StockRelease release : releases) {
    // result += release.getLitres();
    // }
    // return result;
    // }
    //
    // private Double sumRefueledLitres(final List<Refueling> refuelings) {
    // Double result = 0D;
    // for (final Refueling refueling : refuelings) {
    // result += refueling.getLitres();
    // }
    // return result;
    // }
    //
    // private Double sumStockAdditionsLitres(final List<StockAddition> additions) {
    // Double result = 0D;
    // for (final StockAddition addition : additions) {
    // result += addition.getLitres();
    // }
    // return result;
    // }
}
