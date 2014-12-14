package biz.suckow.fuel.business.refueling.control;

/*
 * #%L
 * fuelservice
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

import static org.assertj.guava.api.Assertions.assertThat;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.PersistenceSupport;
import biz.suckow.fuel.business.TestHelper;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class FuelStockLocatorIT extends PersistenceSupport {

    @Test
    public void locateMustSucceed() {
	Owner duke = TestHelper.createDuke();
	em.persist(duke);

	Vehicle dukeCar = TestHelper.createDukeCar(duke);
	em.persist(dukeCar);

	FuelStock stock = new FuelStock();
	stock.setVehicle(dukeCar);
	em.persist(stock);

	Optional<FuelStock> actualResult = new FuelStockLocator(em).locate(dukeCar);
	assertThat(actualResult).isPresent().contains(stock);
    }
}