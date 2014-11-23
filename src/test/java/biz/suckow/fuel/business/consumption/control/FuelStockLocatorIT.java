package biz.suckow.fuel.business.consumption.control;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class FuelStockLocatorIT extends ArquillianBase {
    @Inject
    private FuelStockLocator cut;

    @Test
    public void f() {
        final Owner owner = new Owner();
        owner.setOwnername("duke");
        this.em.persist(owner);

        final Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehiclename("duke-car");
        this.em.persist(vehicle);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        final Date january = calendar.getTime();
        calendar.set(Calendar.MONTH, 1);
        final Date february = calendar.getTime();
        calendar.set(Calendar.MONTH, 2);
        final Date march = calendar.getTime();
        final StockAddition additionFebruary = new StockAddition().setDateAdded(february)
                .setEurosPerLitre(1D)
                .setLitres(1D);
        this.em.persist(additionFebruary);
        final StockAddition additionMarch = new StockAddition().setDateAdded(march).setEurosPerLitre(1D).setLitres(1D);
        this.em.persist(additionMarch);

        FuelStock stock = new FuelStock().setVehicle(vehicle);
        this.em.persist(stock);
        stock.add(additionFebruary).add(additionMarch);
        stock = this.em.merge(stock);

        final List<StockAddition> actualResult = this.cut.getAdditionsBetween(january, march, vehicle);
        assertThat(actualResult).hasSize(1).contains(additionFebruary);
    }
}
