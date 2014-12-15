package biz.suckow.fuelservice.business.consumption.control;

/*
 * #%L
 * fuel
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
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuelservice.business.refueling.entity.FuelStock;
import biz.suckow.fuelservice.business.refueling.entity.StockAddition;
import biz.suckow.fuelservice.business.refueling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

public class FuelStockLocator {
    private final EntityManager em;

    @Inject
    public FuelStockLocator(final EntityManager em) {
	this.em = em;
    }

    public List<StockAddition> getAdditionsBetween(final Date left, final Date right, final Vehicle vehicle) {
	final List<StockAddition> result = this.em
		.createNamedQuery(FuelStock.FIND_ADDITIONS_BY_VEHICLE_AND_DATE_BETWEEN, StockAddition.class)
		.setParameter("left", left).setParameter("right", right).setParameter("vehicle", vehicle)
		.getResultList();
	return result;
    }

    public List<StockRelease> getReleasesBetween(final Date left, final Date right, final Vehicle vehicle) {
	final List<StockRelease> result = this.em
		.createNamedQuery(FuelStock.FIND_RELEASES_BY_VEHCILE_AND_DATE_BETWEEN, StockRelease.class)
		.setParameter("left", left).setParameter("right", right).setParameter("vehicle", vehicle)
		.getResultList();
	return result;
    }

}
