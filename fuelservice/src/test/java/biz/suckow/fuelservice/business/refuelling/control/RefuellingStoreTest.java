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

import static org.easymock.EasyMock.cmp;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

import javax.persistence.EntityManager;

import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class RefuellingStoreTest extends EasyMockSupport {
    @Mock
    private EntityManager emMock;

    @BeforeClass
    private void BeforeClass() {
	injectMocks(this);
    }

    private final Comparator<Refuelling> refuelingComparator = (o1, o2) -> {
	if (Objects.equals(o1.getEurosPerLitre(), o2.getEurosPerLitre())
		&& Objects.equals(o1.getLitres(), o2.getLitres())
		&& Objects.equals(o1.getKilometre(), o2.getKilometre()) && Objects.equals(o1.getMemo(), o2.getMemo())
		&& Objects.equals(o1.getDateRefueled(), o2.getDateRefueled())
		&& Objects.equals(o1.getIsFillUp(), o2.getIsFillUp())
		&& Objects.equals(o1.getVehicle(), o2.getVehicle())) {
	    return 0;
	}
	return -1;
    };

    @Test
    public void storePartialRefuelingMustPersistExactly() {
	final Double expectedEuros = 1.22D;
	final Double expectedLitres = 42D;
	final String expectedMemo = "duke";
	final Date expectedDate = new Date();
	final Vehicle expectedVehicle = new Vehicle().setOwner(new Owner().setEmail("duke")).setVehicleName(
		"duke-car");
	final Refuelling expectedRefuelling = new Refuelling.Builder().eurosPerLitre(expectedEuros).litres(expectedLitres)
		.memo(expectedMemo).dateRefueled(expectedDate).fillUp(false).vehicle(expectedVehicle).build();

	this.resetAll();
	this.emMock.persist(cmp(expectedRefuelling, this.refuelingComparator, LogicalOperator.EQUAL));
	expectLastCall();
	this.replayAll();

	new RefuellingStore(this.emMock).storePartialRefueling(expectedVehicle, expectedEuros, expectedLitres,
		expectedMemo, expectedDate);
	this.verifyAll();
    }

    @Test
    public void storeFillUpMustPersistExactly() {
	final Double expectedEuros = 1.22D;
	final Double expectedLitres = 42D;
	final Double expectedKilometres = 120000D;
	final String expectedMemo = "duke";
	final Date expectedDate = new Date();
	final Vehicle expectedVehicle = new Vehicle().setOwner(new Owner().setEmail("duke")).setVehicleName(
		"duke-car");

	final Refuelling expectedRefuelling = new Refuelling.Builder().eurosPerLitre(expectedEuros).litres(expectedLitres)
		.kilometre(expectedKilometres).memo(expectedMemo).dateRefueled(expectedDate).fillUp(true)
		.vehicle(expectedVehicle).build();

	this.resetAll();
	this.emMock.persist(cmp(expectedRefuelling, this.refuelingComparator, LogicalOperator.EQUAL));
	this.replayAll();

	new RefuellingStore(this.emMock).storeFillUp(expectedVehicle, expectedEuros, expectedLitres, expectedKilometres,
		expectedMemo, expectedDate);
	this.verifyAll();
    }
}
