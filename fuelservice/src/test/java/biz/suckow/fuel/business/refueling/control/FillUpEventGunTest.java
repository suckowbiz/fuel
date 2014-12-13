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

import static org.easymock.EasyMock.cmp;

import java.util.Comparator;

import javax.enterprise.event.Event;

import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.consumption.entity.FillUpEvent;

public class FillUpEventGunTest extends EasyMockSupport {
    @Mock
    private Event<FillUpEvent> fillUpEventMock;

    private FillUpEventGun cut;

    @BeforeClass
    private void setUp() {
	injectMocks(this);
	this.cut = new FillUpEventGun(this.fillUpEventMock);
    }

    @Test(expectedExceptions=NullPointerException.class)
    public void fireMustFail() {
	this.cut.fire(null);
    }

    @Test
    public void fireMustTriggerEvent() {
	FillUpEvent expectedEvent = new FillUpEvent().setRefuelingId(42L);
	Comparator<FillUpEvent> eventComparator = new Comparator<FillUpEvent>() {
	    @Override
	    public int compare(FillUpEvent o1, FillUpEvent o2) {
		if (o1.getRefuelingId().equals(o2.getRefuelingId()))
		    return 0;
		return 1;
	    }
	};

	this.resetAll();
	this.fillUpEventMock.fire(cmp(expectedEvent, eventComparator, LogicalOperator.EQUAL));
	this.replayAll();

	this.cut.fire(expectedEvent.getRefuelingId());
	this.verifyAll();
    }
}
