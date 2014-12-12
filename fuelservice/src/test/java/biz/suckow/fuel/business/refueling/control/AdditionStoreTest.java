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
import static org.easymock.EasyMock.expectLastCall;

import java.util.Comparator;
import java.util.Date;

import javax.persistence.EntityManager;

import org.easymock.EasyMockSupport;
import org.easymock.LogicalOperator;
import org.easymock.Mock;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.refueling.entity.StockAddition;

public class AdditionStoreTest extends EasyMockSupport {

    @Mock
    private EntityManager emMock;

    @BeforeClass
    private void beforeClass() {
	injectMocks(this);
    }

    @Test
    public void store() {
	Date expectedDate = new Date();
	Double expectedEuros = 1.20D;
	Double expectedLitres = 42D;
	String expectedMemo = "duke";

	StockAddition expectedAddition = new StockAddition().setDateAdded(expectedDate).setEurosPerLitre(expectedEuros)
		.setLitres(expectedLitres).setMemo(expectedMemo);
	Comparator<StockAddition> additionComparator = new Comparator<StockAddition>() {
	    @Override
	    public int compare(StockAddition o1, StockAddition o2) {
		if (o1.getDateRefueled().equals(o2.getDateRefueled())
			&& o1.getEurosPerLitre().equals(o2.getEurosPerLitre()) && o1.getLitres().equals(o2.getLitres())
			&& o1.getMemo().equals(o2.getMemo()))
		    return 0;
		return -1;
	    }
	};

	this.resetAll();
	this.emMock.persist(cmp(expectedAddition, additionComparator, LogicalOperator.EQUAL));
	expectLastCall();
	this.replayAll();

	new AdditionStore(this.emMock).store(expectedDate, expectedEuros, expectedLitres, expectedMemo);
	this.verifyAll();
    }
}
