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

import biz.suckow.fuelservice.business.refuelling.entity.FuelStock;
import biz.suckow.fuelservice.business.refuelling.entity.StockAddition;
import biz.suckow.fuelservice.business.refuelling.entity.StockRelease;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class FuelStockStore {
    @Inject
    EntityManager em;

    public void addition(final Vehicle vehicle, final Date date, final Double euros, final Double litres) {
        final StockAddition addition = new StockAddition().setDateAdded(date).setEurosPerLitre(euros).setLitres(litres);
        this.em.persist(addition);

        final FuelStock fuelStock = this.getFuelStockOf(vehicle).orElseThrow(() -> new IllegalStateException("Missing fuel stock!"));
        fuelStock.add(addition);
        this.em.merge(fuelStock);

    }

    public void release(final Vehicle vehicle, final Date date, final Double litres) {
        final StockRelease release = new StockRelease().setDateReleased(date).setLitres(litres);
        this.em.persist(release);

        FuelStock fuelStock = this.getFuelStockOf(vehicle).orElseThrow(() -> new IllegalStateException("Missing fuel stock!"));
        fuelStock.release(release);
        this.em.merge(fuelStock);
    }

    public Optional<FuelStock> getFuelStockOf(final Vehicle vehicle) {
        Optional<FuelStock> result = Optional.empty();
        final List<FuelStock> fuelStockItems = this.em.createNamedQuery(FuelStock.FIND_BY_VEHICLE, FuelStock.class)
                .setParameter("vehicle", vehicle).getResultList();
        if (fuelStockItems.size() > 0) {
            result = Optional.of(fuelStockItems.get(0));
        }
        return result;
    }

}
