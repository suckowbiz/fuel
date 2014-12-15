package biz.suckow.fuelservice.business.refueling.control;

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

import static org.easymock.EasyMock.anyString;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Date;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.refueling.control.FillUpEventGun;
import biz.suckow.fuelservice.business.refueling.control.FuelStockStore;
import biz.suckow.fuelservice.business.refueling.control.RefuelingService;
import biz.suckow.fuelservice.business.refueling.control.RefuelingStore;
import biz.suckow.fuelservice.business.refueling.control.VehicleLocator;
import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.refueling.entity.RefuelingMeta;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class RefuelingServiceTest extends EasyMockSupport {
    @Mock
    private RefuelingStore refuelingStoreMock;

    @Mock
    private FuelStockStore storeMock;

    @Mock
    private FillUpEventGun gunMock;

    @Mock
    private VehicleLocator locatorMock;

    private RefuelingService cut;

    @BeforeClass
    private void beforeClass() {
	injectMocks(this);
	this.cut = new RefuelingService(this.refuelingStoreMock, this.storeMock, this.gunMock, this.locatorMock);
    }

    @Test(expectedExceptions=IllegalStateException.class)
    public void addMustFail() {
	this.resetAll();
	expect(this.locatorMock.getVehicle(anyString(), anyString())).andStubReturn(Optional.<Vehicle>absent());
	this.replayAll();
	this.cut.add(null, null, null);
    }

    @Test
    public void fullTankAndStockRefuelMustPersistExactly() {
	Vehicle expectedVehicle = TestHelper.createDukeCar(TestHelper.createDuke());
	RefuelingMeta meta = new RefuelingMeta();
	meta.date = new Date();
	meta.eurosPerLitre = 1D;
	meta.litresToTank = 2D;
	meta.litresToStock = 10D;
	meta.litresFromStock=10D;
	meta.kilometre = 120000D;
	meta.isFull = true;
	meta.memo = "full-with-stock-refueling";

	this.resetAll();
	expect(this.locatorMock.getVehicle(expectedVehicle.getOwner().getOwnername(), expectedVehicle.getVehiclename()))
	.andStubReturn(Optional.<Vehicle> fromNullable(expectedVehicle));
	this.storeMock.addition(expectedVehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
	expectLastCall();
	this.storeMock.release(expectedVehicle, meta.date, meta.litresFromStock);
	expectLastCall();
	expect(
		this.refuelingStoreMock.storeFillUp(expectedVehicle, meta.eurosPerLitre, meta.litresToTank,
			meta.kilometre, meta.memo, meta.date)).andStubReturn(new Refueling());
	this.gunMock.fire(null);
	expectLastCall();
	this.replayAll();

	this.cut.add(expectedVehicle.getVehiclename(), expectedVehicle.getOwner().getOwnername(), meta);
	this.verifyAll();
    }

    @Test
    public void partialTankRefuelMustPersistExactly() {
	Vehicle expectedVehicle = TestHelper.createDukeCar(TestHelper.createDuke());
	RefuelingMeta meta = new RefuelingMeta();
	meta.date = new Date();
	meta.eurosPerLitre = 1D;
	meta.litresToTank = 2D;
	meta.memo = "partial-refueling";

	this.resetAll();
	expect(this.locatorMock.getVehicle(expectedVehicle.getOwner().getOwnername(), expectedVehicle.getVehiclename()))
	.andStubReturn(Optional.<Vehicle> fromNullable(expectedVehicle));
	this.refuelingStoreMock.storePartialRefueling(expectedVehicle, meta.eurosPerLitre, meta.litresToTank,
		meta.memo, meta.date);
	expectLastCall();
	this.replayAll();

	this.cut.add(expectedVehicle.getVehiclename(), expectedVehicle.getOwner().getOwnername(), meta);
	this.verifyAll();
    }

}
