package biz.suckow.fuelservice.business.refueling.control;

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
import java.util.Optional;

import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.PersistenceSupport;
import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class RefuelingLocatorIT extends PersistenceSupport {
    private final RefuelingLocator cut = new RefuelingLocator(em);

    @Test
    public void mustFetchPartialsBetween() {
	final Owner duke = TestHelper.createDuke();
	em.persist(duke);

	final Vehicle dukeCar = TestHelper.createDukeCar(duke);
	em.persist(dukeCar);

	final Vehicle oakCar = new Vehicle().setOwner(duke).setVehicleName("oak-car");
	em.persist(oakCar);

	final Date january = TestHelper.getMonth(0);
	final Date february = TestHelper.getMonth(1);
	final Date march = TestHelper.getMonth(2);
	final Date april = TestHelper.getMonth(3);

	final Refueling partialFebruaryDuke = this.createRefueling(dukeCar, february, false);
	em.persist(partialFebruaryDuke);

	final Refueling fillUpFebruaryDuke = this.createRefueling(dukeCar, february, true);
	em.persist(fillUpFebruaryDuke);

	final Refueling partialFebruaryOak = this.createRefueling(oakCar, february, false);
	em.persist(partialFebruaryOak);

	final Refueling partialMarchDuke = this.createRefueling(dukeCar, march, false);
	em.persist(partialMarchDuke);

	final Refueling fullMarch = this.createRefueling(dukeCar, march, true);
	em.persist(fullMarch);

	final Refueling partialAprilDuke = this.createRefueling(dukeCar, april, false);
	em.persist(partialAprilDuke);

	final List<Refueling> actualRefuelings = this.cut.getPartialRefuelingsBetween(january, april, dukeCar);
	assertThat(actualRefuelings).hasSize(2).containsOnly(partialFebruaryDuke, partialMarchDuke);
    }

    @Test
    public void mustFetchLatestFillUpBefore() {
	final Owner duke = TestHelper.createDuke();
	em.persist(duke);

	final Vehicle dukeCar = TestHelper.createDukeCar(duke);
	em.persist(dukeCar);

	final Vehicle oakCar = new Vehicle().setOwner(duke).setVehicleName("oak-car");
	em.persist(oakCar);

	final Date january = TestHelper.getMonth(0);
	final Date february = TestHelper.getMonth(1);
	final Date march = TestHelper.getMonth(2);
	final Date april = TestHelper.getMonth(3);

	final Refueling fillUpJanuary = this.createRefueling(dukeCar, january, true);
	em.persist(fillUpJanuary);

	final Refueling fillUpFebruary = this.createRefueling(dukeCar, february, true);
	em.persist(fillUpFebruary);

	final Refueling partialFillUpMarch = this.createRefueling(dukeCar, march, false);
	em.persist(partialFillUpMarch);

	final Refueling fillUpApril = this.createRefueling(dukeCar, april, true);
	em.persist(fillUpApril);

	final Optional<Refueling> actualResult = this.cut.getFillUpBefore(april);
	assertThat(actualResult.isPresent());
	assertThat(actualResult.get()).isSameAs(fillUpFebruary);
    }

    private Refueling createRefueling(final Vehicle vehicle, final Date date, final Boolean isFillUp) {
	return new Refueling.Builder().dateRefueled(date).eurosPerLitre(1D).fillUp(isFillUp).kilometre(1D).litres(1D)
		.vehicle(vehicle).build();
    }
}
