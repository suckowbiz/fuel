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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.owner.entity.Owner;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "vehiclename", "owner_id" }))
@NamedQuery(name = Vehicle.QueryByOwnerAndVehicle.NAME, query = "SELECT v FROM Vehicle v WHERE LOWER(v.vehiclename) = "
        + "LOWER(:" + Vehicle.QueryByOwnerAndVehicle.VEHICLENAME + ") AND " + "LOWER(v.owner.ownername) = :"
        + Vehicle.QueryByOwnerAndVehicle.OWNERNAME)
public class Vehicle extends BaseEntity {
    private static final long serialVersionUID = -5360751385120611439L;

    public static final class QueryByOwnerAndVehicle {
        public static final String NAME = "Vehicle.ByOwnerAndVehicle";
        public static final String OWNERNAME = "ownername";
        public static final String VEHICLENAME = "vehiclename";
    }

    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String vehiclename;

    @NotNull
    @ManyToOne(optional = false)
    private Owner owner;

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

    public String getVehiclename() {
        return this.vehiclename;
    }
}
