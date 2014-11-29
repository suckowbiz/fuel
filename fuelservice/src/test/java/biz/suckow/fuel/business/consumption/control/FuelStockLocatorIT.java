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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.EntityFactory;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class FuelStockLocatorIT extends ArquillianBase {
    @Inject
    private FuelStockLocator cut;

    @Test
    public void mustFetchAdditionsBetween() {
        final Vehicle dukeCar = EntityFactory.createdAndPersistOwnerWithCar(this.em);
        final Date january = EntityFactory.getMonth(0);
        final Date february = EntityFactory.getMonth(1);
        final Date march = EntityFactory.getMonth(2);

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
        final Vehicle dukeCar = EntityFactory.createdAndPersistOwnerWithCar(this.em);
        final Date january = EntityFactory.getMonth(0);
        final Date february = EntityFactory.getMonth(1);
        final Date march = EntityFactory.getMonth(2);

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
