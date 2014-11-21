/*
 * Copyright 2014 Tobias Suckow.
 *
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
 */
package biz.suckow.fuel.business.consumption.control;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.StockAddition;
import biz.suckow.fuel.business.refueling.entity.StockRelease;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

/**
 * @author tobias
 */
public class FuelStockLocator {
    @Inject
    private EntityManager em;

    // TODO test
    public List<StockAddition> getAdditionsBetween(final Date left, final Date right, final Vehicle vehicle) {
        final List<StockAddition> result = this.em.createNamedQuery(FuelStock.QueryAdditionsBetween.NAME,
                StockAddition.class)
                .setParameter(FuelStock.QueryAdditionsBetween.PARAM_LEFT_NAME, left)
                .setParameter(FuelStock.QueryAdditionsBetween.PARAM_LEFT_NAME, left)
                .getResultList();
        return result;
    }

    public List<StockRelease> getReleasesBetween(final Date left, final Date right, final Vehicle vehicle) {
        // TODO Auto-generated method stub
        return null;
    }

}
