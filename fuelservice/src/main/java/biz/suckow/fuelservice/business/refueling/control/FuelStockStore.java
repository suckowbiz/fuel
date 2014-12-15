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

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuelservice.business.refueling.entity.FuelStock;
import biz.suckow.fuelservice.business.refueling.entity.StockAddition;
import biz.suckow.fuelservice.business.refueling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

public class FuelStockStore {
    private EntityManager em;
    private FuelStockLocator stockLocator;

    @Inject
    public FuelStockStore(EntityManager em, FuelStockLocator stockLocator) {
	this.em = em;
	this.stockLocator = stockLocator;
    }

    public void addition(Vehicle vehicle, Date date, Double euros, Double litres) {
	Optional<FuelStock> possibleStock = this.stockLocator.locate(vehicle);
	Preconditions.checkState(possibleStock.isPresent());

	final StockAddition addition = new StockAddition().setDateAdded(date).setEurosPerLitre(euros).setLitres(litres);
	this.em.persist(addition);

	FuelStock stock = possibleStock.get();
	stock.add(addition);
	this.em.merge(stock);

    }

    public void release(Vehicle vehicle, Date date, Double litres) {
	Optional<FuelStock> possibleStock = this.stockLocator.locate(vehicle);
	Preconditions.checkState(possibleStock.isPresent());

	final StockRelease release = new StockRelease().setDateReleased(date).setLitres(litres);
	this.em.persist(release);

	FuelStock stock = possibleStock.get();
	stock.release(release);
	this.em.merge(stock);
    }
}
