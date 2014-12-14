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

import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class FuelStockLocator {
    private EntityManager em;

    @Inject
    public FuelStockLocator(EntityManager em) {
	this.em = em;
    }

    public Optional<FuelStock> locate(Vehicle vehicle) {
	Optional<FuelStock> result = Optional.absent();
	List<FuelStock> fuelStockItems = this.em.createNamedQuery(FuelStock.FIND_BY_VEHICLE, FuelStock.class).setParameter("vehicle", vehicle).getResultList();
	if (fuelStockItems.size() > 0) {
	    result = Optional.of(fuelStockItems.get(0));
	}
	return result;
    }
}
