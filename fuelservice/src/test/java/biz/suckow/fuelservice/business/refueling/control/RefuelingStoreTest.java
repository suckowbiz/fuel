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

import static org.easymock.EasyMock.cmp;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.EntityManager;

import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.refueling.control.RefuelingStore;
import biz.suckow.fuelservice.business.refueling.entity.Refueling;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Objects;

public class RefuelingStoreTest extends EasyMockSupport {
    @Mock
    private EntityManager emMock;

    @BeforeClass
    private void BeforeClass() {
	injectMocks(this);
    }

    private Comparator<Refueling> refuelingComparator = new Comparator<Refueling>() {
	@Override
	public int compare(Refueling o1, Refueling o2) {
	    if (Objects.equal(o1.getEurosPerLitre(), o2.getEurosPerLitre())
		    && Objects.equal(o1.getLitres(), o2.getLitres())
		    && Objects.equal(o1.getKilometre(), o2.getKilometre()) && Objects.equal(o1.getMemo(), o2.getMemo())
		    && Objects.equal(o1.getDateRefueled(), o2.getDateRefueled())
		    && Objects.equal(o1.getIsFillUp(), o2.getIsFillUp())
		    && Objects.equal(o1.getVehicle(), o2.getVehicle()))
		return 0;
	    return -1;
	}
    };

    @Test
    public void storePartialRefuelingMustPersistExactly() {
	Double expectedEuros = 1.22D;
	Double expectedLitres = 42D;
	String expectedMemo = "duke";
	Date expectedDate = new Date();
	Vehicle expectedVehicle = new Vehicle().setOwner(new Owner().setOwnername("duke")).setVehicleName("duke-car");
	Refueling expectedRefueling = new Refueling.Builder().eurosPerLitre(expectedEuros).litres(expectedLitres)
		.memo(expectedMemo).dateRefueled(expectedDate).fillUp(false).vehicle(expectedVehicle).build();

	this.resetAll();
	this.emMock.persist(cmp(expectedRefueling, this.refuelingComparator, LogicalOperator.EQUAL));
	expectLastCall();
	this.replayAll();

	new RefuelingStore(this.emMock).storePartialRefueling(expectedVehicle, expectedEuros, expectedLitres,
		expectedMemo, expectedDate);
	this.verifyAll();
    }

    @Test
    public void storeFillUpMustPersistExactly() {
	Double expectedEuros = 1.22D;
	Double expectedLitres = 42D;
	Double expectedKilometres = 120000D;
	String expectedMemo = "duke";
	Date expectedDate = new Date();
	Vehicle expectedVehicle = new Vehicle().setOwner(new Owner().setOwnername("duke")).setVehicleName("duke-car");

	Refueling expectedRefueling = new Refueling.Builder().eurosPerLitre(expectedEuros).litres(expectedLitres)
		.kilometre(expectedKilometres).memo(expectedMemo).dateRefueled(expectedDate).fillUp(true)
		.vehicle(expectedVehicle).build();

	this.resetAll();
	this.emMock.persist(cmp(expectedRefueling, this.refuelingComparator, LogicalOperator.EQUAL));
	this.replayAll();

	new RefuelingStore(this.emMock).storeFillUp(expectedVehicle, expectedEuros, expectedLitres, expectedKilometres,
		expectedMemo, expectedDate);
	this.verifyAll();
    }
}
