package biz.suckow.fuelservice.business.consumption.boundary;

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

import biz.suckow.fuelservice.business.consumption.control.FuelConsumptionCalculator;
import biz.suckow.fuelservice.business.consumption.entity.FillUpEvent;
import biz.suckow.fuelservice.business.consumption.entity.FuelConsumption;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;
import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;

import static org.easymock.EasyMock.*;

public class FillUpEventConsumerTest extends EasyMockSupport {
    @TestSubject
    private final FillUpEventConsumer cut = new FillUpEventConsumer();
    @Mock
    private EntityManager em;
    @Mock
    private FuelConsumptionCalculator maths;

    @BeforeClass
    private void setUp() {
        injectMocks(this);
    }

    @Test
    public void consume() {
        final Refuelling refuelling = new Refuelling().setDateRefueled(new Date()).setVehicle(new Vehicle());
        final FillUpEvent event = new FillUpEvent().setRefuelingId(42L);
        final FuelConsumption expectedConsumption = new FuelConsumption().setDateComputed(new Date())
                .setLitresPerKilometre(6D).setVehicle(refuelling.getVehicle());
        final Comparator<FuelConsumption> consumptionComparator = (o1, o2) -> {
            if (Objects.equals(o1.getVehicle(), o2.getVehicle()) && Objects.equals(o1.getLitresPerKilometre(), 1D)
                    && (o1.getDateComputed() != null && o2.getDateComputed() != null)) {
                return 0;
            }
            return 1;
        };

        this.resetAll();
        expect(this.em.find(Refuelling.class, event.getRefuelingId())).andStubReturn(refuelling);
        expect(this.maths.computeConsumptionFor(refuelling)).andStubReturn(Optional.ofNullable(BigDecimal.ONE));
        this.em.persist(cmp(expectedConsumption, consumptionComparator, LogicalOperator.EQUAL));
        expectLastCall();
        this.replayAll();

        this.cut.consume(event);
        this.verifyAll();
    }
}
