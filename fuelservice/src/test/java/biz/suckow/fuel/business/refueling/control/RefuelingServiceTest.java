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

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Date;

import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class RefuelingServiceTest extends EasyMockSupport {
    @Mock
    private RefuelingStore refuelingStoreMock;

    @Mock
    private AdditionStore additionStoreMock;

    @Mock
    private FillUpEventGun gunMock;

    private RefuelingService cut;

    @BeforeClass
    private void beforeClass() {
	injectMocks(this);
	this.cut = new RefuelingService(this.refuelingStoreMock, this.additionStoreMock, this.gunMock);
    }

    @Test
    public void fullTankAndStockRefuelMustPersistExactly() {
	Double expectedLitresToStock = 10D;
	Double expectedEuros = 1.20D;
	Double expectedLitres = 45D;
	Double expectedKilometres = 120000D;
	String expectedMemo = "duke";
	Date expectedDate = new Date();
	Vehicle expectedVehicle = new Vehicle().setOwner(new Owner().setOwnername("duke")).setVehicleName("duke-car");

	this.resetAll();
	expect(
		this.refuelingStoreMock.storeFillUp(expectedEuros, expectedLitres, expectedKilometres, expectedMemo,
			expectedDate, expectedVehicle)).andStubReturn(new Refueling());
	this.gunMock.fire(null);
	expectLastCall();
	this.additionStoreMock.store(expectedDate, expectedEuros, expectedLitresToStock, expectedMemo);
	this.replayAll();

	this.cut.fullTankAndStockRefuel(expectedVehicle, expectedKilometres, expectedLitres, expectedLitresToStock,
		expectedEuros, expectedDate, expectedMemo);
	this.verifyAll();
    }

    @Test
    public void partialTankRefuelMustPersistExactly() {
	Double expectedEuros = 1D;
	Double expectedLitres = 2D;
	String expectedMemo = "duke";
	Date expectedDate = new Date();
	Vehicle expectedVehicle = new Vehicle().setOwner(new Owner().setOwnername("duke")).setVehicleName("duke-car");

	this.resetAll();
	this.refuelingStoreMock.storePartialRefueling(expectedEuros, expectedLitres, expectedMemo, expectedDate,
		expectedVehicle);
	expectLastCall();
	this.replayAll();

	this.cut.partialTankRefuel(expectedVehicle, expectedLitres, expectedEuros, expectedDate, expectedMemo);
	this.verifyAll();
    }

}
