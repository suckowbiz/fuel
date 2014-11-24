package biz.suckow.fuel.business.consumption.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.TestHelper;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class FuelStockLocatorIT extends ArquillianBase {
    @Inject
    private FuelStockLocator cut;

    @Test
    public void mustFetchAdditionsBetween() {
        final Vehicle dukeCar = TestHelper.getCreatedAndPersistedDukeCar(this.em);
        final Date january = TestHelper.getMonth(0);
        final Date february = TestHelper.getMonth(1);
        final Date march = TestHelper.getMonth(2);

        final StockAddition additionFebruary = new StockAddition().setDateAdded(february)
                .setEurosPerLitre(1D)
                .setLitres(1D);
        this.em.persist(additionFebruary);
        final StockAddition additionMarch = new StockAddition().setDateAdded(march).setEurosPerLitre(1D).setLitres(1D);
        this.em.persist(additionMarch);

        FuelStock stock = new FuelStock().setVehicle(dukeCar);
        this.em.persist(stock);
        stock.add(additionFebruary).add(additionMarch);
        stock = this.em.merge(stock);

        final List<StockAddition> actualResult = this.cut.getAdditionsBetween(january, march, dukeCar);
        assertThat(actualResult).hasSize(1).contains(additionFebruary);
    }

    @Test
    public void mustFetchReleasesBetween() {
        final Vehicle dukeCar = TestHelper.getCreatedAndPersistedDukeCar(this.em);
        final Date january = TestHelper.getMonth(0);
        final Date february = TestHelper.getMonth(1);
        final Date march = TestHelper.getMonth(2);

        final StockRelease releaseFebruary = new StockRelease().setDateReleased(february).setLitres(1D);
        this.em.persist(releaseFebruary);
        final StockRelease releaseMarch = new StockRelease().setDateReleased(march).setLitres(1D);
        this.em.persist(releaseMarch);

        FuelStock stock = new FuelStock().setVehicle(dukeCar);
        this.em.persist(stock);
        stock.release(releaseFebruary).release(releaseMarch);
        stock = this.em.merge(stock);

        final List<StockRelease> actualResult = this.cut.getReleasesBetween(january, march, dukeCar);
        assertThat(actualResult).hasSize(1).contains(releaseFebruary);
    }
}
