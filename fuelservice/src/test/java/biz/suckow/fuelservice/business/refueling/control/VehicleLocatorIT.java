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
import static org.assertj.guava.api.Assertions.assertThat;

import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.PersistenceSupport;
import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.refueling.control.VehicleLocator;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class VehicleLocatorIT extends PersistenceSupport {

    private VehicleLocator cut = new VehicleLocator(em);

    @Test
    public void mustFetchExistingVehicle() {
	Owner duke = TestHelper.createDuke();
	em.persist(duke);

	Vehicle dukeCar = TestHelper.createDukeCar(duke);
	em.persist(dukeCar);

	Vehicle actualResult = this.cut.getVehicle(dukeCar.getOwner().getOwnername(), dukeCar.getVehiclename()).get();
	assertThat(actualResult).isNotNull();
	assertThat(actualResult.getId()).isEqualTo(dukeCar.getId());
    }

    @Test
    public void mustNotFetchNonExistingVehicle() {
	Optional<Vehicle> possibleVehicle = this.cut.getVehicle("duke", "duke-bike");
	assertThat(possibleVehicle).isAbsent();
    }
}
