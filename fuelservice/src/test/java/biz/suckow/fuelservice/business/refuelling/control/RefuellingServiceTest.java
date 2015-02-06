package biz.suckow.fuelservice.business.refuelling.control;

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

import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.refuelling.boundary.RefuellingService;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.vehicle.boundary.VehicleService;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;
import org.easymock.EasyMockSupport;
import org.easymock.Mock;
import org.easymock.TestSubject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Date;
import java.util.Optional;

import static org.easymock.EasyMock.*;

public class RefuellingServiceTest extends EasyMockSupport {
    @Mock
    private RefuellingStore refuellingStore;

    @Mock
    private FuelStockStore stockStore;

    @Mock
    private FillUpEventGun gun;

    @Mock
    private VehicleService vehicleService;

    @TestSubject
    private RefuellingService cut = new RefuellingService();

    @BeforeClass
    private void beforeClass() {
        injectMocks(this);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void addMustFail() {
        this.resetAll();
        expect(this.vehicleService.lookUp(anyString(), anyString())).andStubReturn(Optional.<Vehicle>empty());
        this.replayAll();
        this.cut.add(null, null, null);
    }

    @Test
    public void fullTankAndStockRefuelMustPersistExactly() {
        final Vehicle expectedVehicle = TestHelper.createDukeCar(TestHelper.createDuke());
        final RefuellingMeta meta = new RefuellingMeta();
        meta.date = new Date();
        meta.eurosPerLitre = 1D;
        meta.litresToTank = 2D;
        meta.litresToStock = 10D;
        meta.litresFromStock = 10D;
        meta.kilometre = 120000D;
        meta.isFull = true;
        meta.memo = "full-with-stock-refuelling";

        this.resetAll();
        expect(this.vehicleService.lookUp(expectedVehicle.getOwner().getEmail(), expectedVehicle.getVehicleName()))
                .andStubReturn(Optional.ofNullable(expectedVehicle));
        this.stockStore.addition(expectedVehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
        expectLastCall();
        this.stockStore.release(expectedVehicle, meta.date, meta.litresFromStock);
        expectLastCall();
        expect(
                this.refuellingStore.storeFillUp(expectedVehicle, meta.eurosPerLitre, meta.litresToTank,
                        meta.kilometre, meta.memo, meta.date)).andStubReturn(new Refuelling());
        this.gun.fire(null);
        expectLastCall();
        this.replayAll();

        this.cut.add(expectedVehicle.getVehicleName(), expectedVehicle.getOwner().getEmail(), meta);
        this.verifyAll();
    }

    @Test
    public void partialTankRefuelMustPersistExactly() {
        final Vehicle expectedVehicle = TestHelper.createDukeCar(TestHelper.createDuke());
        final RefuellingMeta meta = new RefuellingMeta();
        meta.date = new Date();
        meta.eurosPerLitre = 1D;
        meta.litresToTank = 2D;
        meta.memo = "partial-refuelling";

        this.resetAll();
        expect(this.vehicleService.lookUp(expectedVehicle.getOwner().getEmail(), expectedVehicle.getVehicleName()))
                .andStubReturn(Optional.ofNullable(expectedVehicle));
        this.refuellingStore.storePartialRefueling(expectedVehicle, meta.eurosPerLitre, meta.litresToTank,
                meta.memo, meta.date);
        expectLastCall();
        this.replayAll();

        this.cut.add(expectedVehicle.getVehicleName(), expectedVehicle.getOwner().getEmail(), meta);
        this.verifyAll();
    }

}
