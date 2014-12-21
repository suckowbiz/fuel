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

import static org.assertj.core.api.Assertions.assertThat;
import static org.easymock.EasyMock.anyObject;
import static org.easymock.EasyMock.cmp;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;

import java.util.Comparator;
import java.util.Date;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.refueling.entity.FuelStock;
import biz.suckow.fuelservice.business.refueling.entity.StockAddition;
import biz.suckow.fuelservice.business.refueling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class FuelStockStoreTest extends EasyMockSupport {
    @Mock
    private EntityManager emMock;

    @Mock
    private FuelStockLocator locatorMock;

    private FuelStockStore cut;

    @BeforeClass
    private void beforeClass() {
	injectMocks(this);
	this.cut = new FuelStockStore(this.emMock, this.locatorMock);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void releaseMustFail() {
	this.resetAll();
	this.emMock.persist(anyObject(StockRelease.class));
	expectLastCall();
	expect(this.locatorMock.locate(anyObject(Vehicle.class))).andStubReturn(Optional.<FuelStock> empty());
	this.replayAll();

	this.cut.release(null, null, null);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void additionMustFail() {
	this.resetAll();
	this.emMock.persist(anyObject(StockAddition.class));
	expectLastCall();
	expect(this.locatorMock.locate(anyObject(Vehicle.class))).andStubReturn(Optional.<FuelStock> empty());
	this.replayAll();

	this.cut.addition(null, null, null, null);
    }

    @Test
    public void additionMustSucceed() {
	final Date expectedDate = new Date();
	final Double expectedEuros = 1.20D;
	final Double expectedLitres = 42D;
	final Owner duke = TestHelper.createDuke();
	final Vehicle dukeCar = TestHelper.createDukeCar(duke);
	final FuelStock fuelStock = new FuelStock();
	final StockAddition expectedAddition = new StockAddition().setDateAdded(expectedDate)
		.setEurosPerLitre(expectedEuros).setLitres(expectedLitres);
	final Comparator<StockAddition> additionComparator = (o1, o2) -> {
	    if (o1.getDateRefueled().equals(o2.getDateRefueled())
		    && o1.getEurosPerLitre().equals(o2.getEurosPerLitre()) && o1.getLitres().equals(o2.getLitres())) {
		return 0;
	    }
	    return -1;
	};

	this.resetAll();
	expect(this.locatorMock.locate(dukeCar)).andStubReturn(Optional.ofNullable(fuelStock));
	this.emMock.persist(cmp(expectedAddition, additionComparator, LogicalOperator.EQUAL));
	expectLastCall();
	expect(this.emMock.merge(fuelStock)).andStubReturn(null);
	this.replayAll();

	this.cut.addition(dukeCar, expectedDate, expectedEuros, expectedLitres);
	assertThat(fuelStock.getAdditions()).hasSize(1);
	assertThat(fuelStock.getAdditions().iterator().next().getDateRefueled()).isEqualTo(expectedDate);
	assertThat(fuelStock.getAdditions().iterator().next().getEurosPerLitre()).isEqualTo(expectedEuros);
	assertThat(fuelStock.getAdditions().iterator().next().getLitres()).isEqualTo(expectedLitres);
	this.verifyAll();
    }

    @Test
    public void releaseMustSucceed() {
	final Date expectedDate = new Date();
	final Double expectedLitres = 42D;
	final Owner duke = TestHelper.createDuke();
	final Vehicle dukeCar = TestHelper.createDukeCar(duke);
	final FuelStock fuelStock = new FuelStock();
	final StockRelease expectedRelease = new StockRelease().setDateReleased(expectedDate).setLitres(expectedLitres);
	final Comparator<StockRelease> releaseComparator = (o1, o2) -> {
	    if (o1.getDateReleased().equals(o2.getDateReleased()) && o1.getLitres().equals(o2.getLitres())) {
		return 0;
	    }
	    return -1;
	};

	this.resetAll();
	expect(this.locatorMock.locate(dukeCar)).andStubReturn(Optional.ofNullable(fuelStock));
	this.emMock.persist(cmp(expectedRelease, releaseComparator, LogicalOperator.EQUAL));
	expectLastCall();
	expect(this.emMock.merge(fuelStock)).andStubReturn(null);
	this.replayAll();

	this.cut.release(dukeCar, expectedDate, expectedLitres);
	assertThat(fuelStock.getReleases()).hasSize(1);
	assertThat(fuelStock.getReleases().iterator().next().getDateReleased()).isEqualTo(expectedDate);
	assertThat(fuelStock.getReleases().iterator().next().getLitres()).isEqualTo(expectedLitres);
	this.verifyAll();
    }
}
