package biz.suckow.fuel.business;

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

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;

import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class EntityFactory {
    public static final String DEFAULT_OWNER_NAME = "duke";
    public static final String DEFAULT_VEHICLE_NAME = "duke-car";

    public static Vehicle createdAndPersistOwnerWithCar(final EntityManager em) {
        final Owner owner = new Owner().setOwnername(EntityFactory.DEFAULT_OWNER_NAME);
        em.persist(owner);

        final Vehicle vehicle = new Vehicle().setOwner(owner).setVehicleName(EntityFactory.DEFAULT_VEHICLE_NAME);
        em.persist(vehicle);

        return vehicle;
    }

    public static Date getMonth(final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();
    }
}
