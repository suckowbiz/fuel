package biz.suckow.fuelservice.business.refueling.entity;

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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import biz.suckow.fuelservice.business.app.entity.BaseEntity;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

@Entity
@NamedQueries({
    @NamedQuery(name = FuelStock.FIND_BY_VEHICLE, query = "SELECT fs FROM FuelStock fs WHERE fs.vehicle = :vehicle"),
    @NamedQuery(name = FuelStock.FIND_ADDITIONS_BY_VEHICLE_AND_DATE_BETWEEN, query = "SELECT a FROM FuelStock fs JOIN fs.additions a JOIN fs.vehicle v WHERE v = :vehicle AND a.dateAdded > :left AND a.dateAdded < :right"),
    @NamedQuery(name = FuelStock.FIND_RELEASES_BY_VEHCILE_AND_DATE_BETWEEN, query = "SELECT sr FROM FuelStock fs JOIN fs.releases sr WHERE fs.vehicle = :vehicle "
	    + "AND sr.dateReleased > :left AND sr.dateReleased < :right") })
public class FuelStock extends BaseEntity {
    private static final long serialVersionUID = 2386152541780890783L;

    private static final String PREFIX = "biz.suckow.fuelservice.business.refueling.entity";
    public static final String FIND_BY_VEHICLE = FuelStock.PREFIX + "findByVehicle";
    public static final String FIND_ADDITIONS_BY_VEHICLE_AND_DATE_BETWEEN = FuelStock.PREFIX + "findAdditionsBetween";
    public static final String FIND_RELEASES_BY_VEHCILE_AND_DATE_BETWEEN = FuelStock.PREFIX + "findReleasesBetween";

    @OneToMany
    private Set<StockAddition> additions;

    @OneToMany
    private Set<StockRelease> releases;

    @NotNull
    @OneToOne
    private Vehicle vehicle;

    public FuelStock() {
	this.releases = new HashSet<>();
	this.additions = new HashSet<>();
    }

    public FuelStock add(final StockAddition addition) {
	this.additions.add(addition);
	return this;
    }

    public Set<StockAddition> getAdditions() {
	return this.additions;
    }

    public Set<StockRelease> getReleases() {
	return this.releases;
    }

    public FuelStock release(final StockRelease release) {
	this.releases.add(release);
	return this;
    }

    public Vehicle getVehicle() {
	return this.vehicle;
    }

    public FuelStock setVehicle(final Vehicle vehicle) {
	this.vehicle = vehicle;
	return this;
    }

}
