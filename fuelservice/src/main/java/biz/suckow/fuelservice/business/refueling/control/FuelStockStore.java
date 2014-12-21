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
import java.util.Optional;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuelservice.business.refueling.entity.FuelStock;
import biz.suckow.fuelservice.business.refueling.entity.StockAddition;
import biz.suckow.fuelservice.business.refueling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class FuelStockStore {
    private final EntityManager em;
    private final FuelStockLocator stockLocator;

    @Inject
    public FuelStockStore(final EntityManager em, final FuelStockLocator stockLocator) {
	this.em = em;
	this.stockLocator = stockLocator;
    }

    public void addition(final Vehicle vehicle, final Date date, final Double euros, final Double litres) {
	final StockAddition addition = new StockAddition().setDateAdded(date).setEurosPerLitre(euros).setLitres(litres);
	this.em.persist(addition);

	final FuelStock fuelStock = this.getFuelStockOf(vehicle);
	fuelStock.add(addition);
	this.em.merge(fuelStock);

    }

    public void release(final Vehicle vehicle, final Date date, final Double litres) {
	final StockRelease release = new StockRelease().setDateReleased(date).setLitres(litres);
	this.em.persist(release);

	final FuelStock fuelStock = this.getFuelStockOf(vehicle);
	fuelStock.release(release);
	this.em.merge(fuelStock);
    }

    private FuelStock getFuelStockOf(final Vehicle vehicle) {
	final Optional<FuelStock> possibleStock = this.stockLocator.locate(vehicle);
	return possibleStock.orElseThrow(() -> new IllegalStateException("Missing fuel stock!"));
    }
}
