package biz.suckow.fuelservice.business.refuelling.control;

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

import biz.suckow.fuelservice.business.PersistenceSupport;
import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.refuelling.entity.FuelStock;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;
import org.testng.annotations.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class FuelStockLocatorIT extends PersistenceSupport {

    @Test
    public void locateMustSucceed() {
        final Owner duke = TestHelper.createDuke();
        em.persist(duke);

        final Vehicle dukeCar = TestHelper.createDukeCar(duke);
        em.persist(dukeCar);

        final FuelStock stock = new FuelStock();
        stock.setVehicle(dukeCar);
        em.persist(stock);

        final Optional<FuelStock> actualResult = new FuelStockLocator(em).locate(dukeCar);
        assertThat(actualResult.isPresent());
        assertThat(actualResult.get()).isSameAs(stock);
    }
}
