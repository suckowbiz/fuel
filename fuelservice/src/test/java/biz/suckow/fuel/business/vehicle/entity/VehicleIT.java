package biz.suckow.fuel.business.vehicle.entity;

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

import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.PersistenceIT;
import biz.suckow.fuel.business.EntityFactory;

/**
 * Integration tests for {@link Vehicle}.
 */
public class VehicleIT extends PersistenceIT {
    @Test(description = "The vehicle id must stay unique per owner to be able to identify an owners car by name.")
    public void vehicleNameMustBeUniqueForOwner() {
        EntityFactory.createdAndPersistOwnerWithCar(PersistenceIT.em);
        try {
            EntityFactory.createdAndPersistOwnerWithCar(PersistenceIT.em);
        } catch (final Throwable t) {
            assertThat(t).hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }
}
