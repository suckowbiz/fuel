package biz.suckow.fuel.business.refueling.control;

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

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.EntityFactory;
import biz.suckow.fuel.business.PersistenceIT;
import biz.suckow.fuel.business.refueling.control.RefuelingLocator;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class RefuelingLocatorIT extends PersistenceIT {
//public class RefuelingLocatorIT extends ArquillianBase { 
//    @Inject
	private RefuelingLocator cut = new RefuelingLocator(em);

    @Test
    public void mustFetchPartialsBetween() {
	em.getTransaction().begin();
        final Vehicle dukeCar = EntityFactory.createdAndPersistOwnerWithCar(em);

        final Vehicle oakCar = new Vehicle().setOwner(dukeCar.getOwner()).setVehicleName("oak-car");
        em.persist(oakCar);

        final Date january = EntityFactory.getMonth(0);
        final Date february = EntityFactory.getMonth(1);
        final Date march = EntityFactory.getMonth(2);
        final Date april = EntityFactory.getMonth(3);

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
        em.getTransaction().setRollbackOnly();
//        em.getTcommit();
        
        final List<Refueling> actualRefuelings = this.cut.getPartialRefuelingsBetween(january, april, dukeCar);
        assertThat(actualRefuelings).hasSize(2).containsOnly(partialFebruaryDuke, partialMarchDuke);
    }

    @Test
    public void mustFetchLatestFillUpBefore() {
	em.getTransaction().begin();
        final Vehicle dukeCar = EntityFactory.createdAndPersistOwnerWithCar(em);

        final Vehicle oakCar = new Vehicle().setOwner(dukeCar.getOwner()).setVehicleName("oak-car");
        em.persist(oakCar);

        final Date january = EntityFactory.getMonth(0);
        final Date february = EntityFactory.getMonth(1);
        final Date march = EntityFactory.getMonth(2);
        final Date april = EntityFactory.getMonth(3);

        final Refueling fillUpJanuary = this.createRefueling(dukeCar, january, true);
        em.persist(fillUpJanuary);

        final Refueling fillUpFebruary = this.createRefueling(dukeCar, february, true);
        em.persist(fillUpFebruary);

        final Refueling partialFillUpMarch = this.createRefueling(dukeCar, march, false);
        em.persist(partialFillUpMarch);

        final Refueling fillUpApril = this.createRefueling(dukeCar, april, true);
        em.persist(fillUpApril);
        em.getTransaction().setRollbackOnly();
        
        final Optional<Refueling> actualResult = this.cut.getFillUpBefore(april);
        assertThat(actualResult).isPresent().contains(fillUpFebruary);
    }

    private Refueling createRefueling(final Vehicle vehicle, final Date date, final Boolean isFillUp) {
        return new Refueling.Builder().dateRefueled(date)
                .eurosPerLitre(1D)
                .fillUp(isFillUp)
                .kilometre(1D)
                .litres(1D)
                .vehicle(vehicle)
                .build();
    }
}
