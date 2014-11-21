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
package biz.suckow.fuel.business.vehicle.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "vehiclename", "owner_id" }))
@NamedQuery(name = Vehicle.QueryByOwnerAndVehicle.NAME,
query = "SELECT v FROM Vehicle v WHERE LOWER(v.vehiclename) = LOWER(:"
        + Vehicle.QueryByOwnerAndVehicle.VEHICLENAME + ") AND LOWER(v.owner.ownername) = :"
        + Vehicle.QueryByOwnerAndVehicle.OWNERNAME)
public class Vehicle extends BaseEntity {
    public String getVehiclename() {
        return this.vehiclename;
    }

    private static final long serialVersionUID = -5360751385120611439L;

    public static final class QueryByOwnerAndVehicle {
        public static final String NAME = "Vehicle.ByOwnerAndVehicle";
        public static final String OWNERNAME = "ownername";
        public static final String VEHICLENAME = "vehiclename";
    }

    @Column(nullable = false)
    private String vehiclename;

    @ManyToOne(optional = false)
    private Owner owner;

    @OneToOne
    @JoinColumn(unique = true)
    private FuelStock fuelStock;

    @OneToMany(mappedBy = "vehicle")
    private Set<Refueling> refuelings;

    @OneToMany
    private Set<FuelConsumption> fuelConsumptions;

    public Vehicle() {
        this.fuelConsumptions = new HashSet<>();
        this.refuelings = new HashSet<>();
    }

    public Vehicle setVehiclename(final String vehiclename) {
        this.vehiclename = vehiclename;
        return this;
    }

    public Owner getOwner() {
        return this.owner;
    }

    public Vehicle setOwner(final Owner owner) {
        this.owner = owner;
        return this;
    }

    public Vehicle addFuelConsuption(final FuelConsumption consumption) {
        this.fuelConsumptions.add(consumption);
        return this;
    }

    public Vehicle addRefueling(final Refueling refueling) {
        this.refuelings.add(refueling);
        return this;
    }

    public FuelStock getFuelStock() {
        return this.fuelStock;
    }

    public Set<Refueling> getRefuelings() {
        return this.refuelings;
    }

    public Set<FuelConsumption> getFuelConsumptions() {
        return this.fuelConsumptions;
    }
}
