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

import org.testng.annotations.Test;

import biz.suckow.fuel.business.PersistenceSupport;
import biz.suckow.fuel.business.TestHelper;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class FuelStockLocatorIT extends PersistenceSupport {
    private FuelStockLocator cut = new FuelStockLocator(em);

    @Test
    public void mustFetchAdditionsBetween() {
	Owner duke = TestHelper.createDuke();
	em.persist(duke);
	
	Vehicle dukeCar = TestHelper.createDukeCar(duke);
	em.persist(dukeCar);
	
	Date january = TestHelper.getMonth(0);
	Date february = TestHelper.getMonth(1);
	Date march = TestHelper.getMonth(2);

	StockAddition additionFebruary = new StockAddition().setDateAdded(february).setEurosPerLitre(1D).setLitres(1D);
	em.persist(additionFebruary);
	
	StockAddition additionMarch = new StockAddition().setDateAdded(march).setEurosPerLitre(1D).setLitres(1D);
	em.persist(additionMarch);

	FuelStock stock = new FuelStock().setVehicle(dukeCar);
	em.persist(stock);
	
	stock.add(additionFebruary).add(additionMarch);
	stock = em.merge(stock);

	List<StockAddition> actualResult = this.cut.getAdditionsBetween(january, march, dukeCar);
	assertThat(actualResult).hasSize(1).contains(additionFebruary);
    }

    @Test
    public void mustFetchReleasesBetween() {
	Owner duke = TestHelper.createDuke();
	em.persist(duke);
	
	Vehicle dukeCar = TestHelper.createDukeCar(duke);
	em.persist(dukeCar);
	
	Date january = TestHelper.getMonth(0);
	Date february = TestHelper.getMonth(1);
	Date march = TestHelper.getMonth(2);

	StockRelease releaseFebruary = new StockRelease().setDateReleased(february).setLitres(1D);
	em.persist(releaseFebruary);
	
	StockRelease releaseMarch = new StockRelease().setDateReleased(march).setLitres(1D);
	em.persist(releaseMarch);

	FuelStock stock = new FuelStock().setVehicle(dukeCar);
	em.persist(stock);
	
	stock.release(releaseFebruary).release(releaseMarch);
	stock = em.merge(stock);

	List<StockRelease> actualResult = this.cut.getReleasesBetween(january, march, dukeCar);
	assertThat(actualResult).hasSize(1).contains(releaseFebruary);
    }
}
