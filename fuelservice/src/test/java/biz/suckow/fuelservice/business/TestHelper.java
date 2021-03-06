package biz.suckow.fuelservice.business;

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

import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.owner.entity.Role;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

public class TestHelper {
    protected static AtomicInteger uniqueness = new AtomicInteger(0);

    public static Owner createDuke() {
        return new Owner().addRole(Role.OWNER).setEmail("duke@java.com").setPassword("42");
    }

    public static Vehicle createDukeCar(Owner owner) {
        return new Vehicle().setOwner(owner).setVehicleName("duke-car");
    }

    public static String getUniqueness() {
        return String.valueOf(uniqueness.getAndAdd(1));
    }

    public static Date getMonth(final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();
    }
}
