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

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.refueling.entity.StockAddition;

public class AdditionStore {
    private EntityManager em;

    @Inject
    public AdditionStore(EntityManager em) {
	this.em = em;
    }

    public void store(Date date, Double euros, Double litres, String memo) {
	final StockAddition addition = new StockAddition().setDateAdded(date).setEurosPerLitre(euros).setLitres(litres)
		.setMemo(memo);
	this.em.persist(addition);
    }

}
