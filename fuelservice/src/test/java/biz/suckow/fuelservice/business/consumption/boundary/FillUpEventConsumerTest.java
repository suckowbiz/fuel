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

import static org.easymock.EasyMock.cmp;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;

import javax.persistence.EntityManager;

import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.consumption.boundary.FillUpEventConsumer;
import biz.suckow.fuelservice.business.consumption.control.FuelConsumptionCalculator;
import biz.suckow.fuelservice.business.consumption.entity.FillUpEvent;
import biz.suckow.fuelservice.business.consumption.entity.FuelConsumption;
import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Objects;
import com.google.common.base.Optional;

public class FillUpEventConsumerTest extends EasyMockSupport {
    @Mock
    private EntityManager em;

    @Mock
    private FuelConsumptionCalculator maths;

    @TestSubject
    private FillUpEventConsumer cut = new FillUpEventConsumer();

    @BeforeClass
    private void setUp() {
	injectMocks(this);
    }

    @Test
    public void consume() {
	Refueling refueling = new Refueling().setDateRefueled(new Date()).setVehicle(new Vehicle());
	FillUpEvent event = new FillUpEvent().setRefuelingId(42L);
	FuelConsumption expectedConsumption = new FuelConsumption().setDateComputed(new Date())
		.setLitresPerKilometre(6D).setVehicle(refueling.getVehicle());
	Comparator<FuelConsumption> consumptionComparator = new Comparator<FuelConsumption>() {
	    @Override
	    public int compare(FuelConsumption o1, FuelConsumption o2) {
		if (Objects.equal(o1.getVehicle(), o2.getVehicle()) &&  Objects.equal(o1.getLitresPerKilometre(), 1D)
			&& (o1.getDateComputed() != null && o2.getDateComputed() != null))
		    return 0;
		return 1;
	    }
	};

	this.resetAll();
	expect(this.em.find(Refueling.class, event.getRefuelingId())).andStubReturn(refueling);
	expect(this.maths.computeConsumptionFor(refueling)).andStubReturn(Optional.fromNullable(BigDecimal.ONE));
	this.em.persist(cmp(expectedConsumption, consumptionComparator, LogicalOperator.EQUAL));
	expectLastCall();
	this.replayAll();

	this.cut.consume(event);
	this.verifyAll();
    }
}
