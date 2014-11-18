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
package biz.suckow.fuel.business.refueling.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@Entity
@NamedQueries({
        @NamedQuery(name = FuelStock.QueryRefuelingsBetween.NAME,
                query = "SELECT r FROM FuelStock fs JOIN fs.refuelings r WHERE fs.vehicle = :vehicle "
                        + "AND r.dateRefueled > :left AND r.dateRefueled < :right"),
        @NamedQuery(name = FuelStock.QueryReleasesBetween.NAME,
                    query = "SELECT sr FROM FuelStock fs JOIN fs.stockReleases sr WHERE fs.vehicle = :vehicle "
                            + "AND sr.dateReleased > :left AND sr.dateReleased < :right") })
public class FuelStock extends BaseEntity {
    private static final long serialVersionUID = 2386152541780890783L;
    // TODO ensuer only one fuelstock per vehcile
    @OneToMany
    private final Set<Refueling> refuelings;

    @OneToMany
    private final Set<StockRelease> stockReleases;

    @OneToOne(mappedBy = "fuelStock")
    private Vehicle vehicle;

    public static final class QueryRefuelingsBetween {
        public static final String NAME = "Refueling.refuelingsBetween";
        public static final String PARAM_LEFT_NAME = "Refueling.refuelingsBetweenLeftParam";
        public static final String PARAM_RIGHT_NAME = "Refueling.refuelingsBetweenRightParam";
    }

    public static final class QueryReleasesBetween {
        public static final String NAME = "Refueling.releasesBetween";
        public static final String PARAM_LEFT_NAME = "Refueling.releasesBetweenLeftParam";
        public static final String PARAM_RIGHT_NAME = "Refueling.releasesBetweenRightParam";
    }

    public FuelStock() {
        this.stockReleases = new HashSet<>();
        this.refuelings = new HashSet<>();
    }

    public void addRefueling(final Refueling refueling) {
        this.refuelings.add(refueling);
    }

    public void addStockRelease(final StockRelease out) {
        this.stockReleases.add(out);
    }

    public Set<Refueling> getRefuelings() {
        return this.refuelings;
    }

    public Set<StockRelease> getStockReleases() {
        return this.stockReleases;
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public FuelStock setVehicle(final Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

}
