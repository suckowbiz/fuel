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
        @NamedQuery(name = FuelStock.QueryAdditionsBetween.NAME,
                query = "SELECT a FROM FuelStock fs JOIN fs.additions a WHERE fs.vehicle = :vehicle"
                        + " AND a.dateAdded > :" + FuelStock.QueryAdditionsBetween.DATE_LEFT + " AND a.dateAdded < :"
                        + FuelStock.QueryAdditionsBetween.DATE_RIGHT),
        @NamedQuery(name = FuelStock.QueryReleasesBetween.NAME,
                query = "SELECT sr FROM FuelStock fs JOIN fs.releases sr WHERE fs.vehicle = :vehicle "
                        + "AND sr.dateReleased > :" + FuelStock.QueryReleasesBetween.DATE_LEFT
                        + " AND sr.dateReleased < :" + FuelStock.QueryReleasesBetween.DATE_RIGHT) })
public class FuelStock extends BaseEntity {
    private static final long serialVersionUID = 2386152541780890783L;

    @OneToMany
    private Set<StockAddition> additions;

    @OneToMany
    private Set<StockRelease> releases;

    @OneToOne(mappedBy = "fuelStock")
    private Vehicle vehicle;

    public static final class QueryAdditionsBetween {
        public static final String NAME = "FuelStock.refuelingsBetween";
        public static final String DATE_LEFT = "dateLeft";
        public static final String DATE_RIGHT = "dateRight";
    }

    public static final class QueryReleasesBetween {
        public static final String NAME = "FuelStock.releasesBetween";
        public static final String DATE_LEFT = "dateLeft";
        public static final String DATE_RIGHT = "dateRight";
    }

    public FuelStock() {
        this.releases = new HashSet<>();
        this.additions = new HashSet<>();
    }

    public void add(final StockAddition addition) {
        this.additions.add(addition);
    }

    public void release(final StockRelease release) {
        this.releases.add(release);
    }

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public FuelStock setVehicle(final Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

}
