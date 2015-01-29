package biz.suckow.fuelservice.business.vehicle.entity;

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

import biz.suckow.fuelservice.business.BaseEntity;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"vehicleName", "owner_id"}))
@NamedQuery(name = Vehicle.QueryByEmailAndVehicleName.NAME, query = "SELECT v FROM Vehicle v WHERE v.vehicleName = :" + Vehicle.QueryByEmailAndVehicleName.VEHICLE_NAME + " AND v.owner.email = :" + Vehicle.QueryByEmailAndVehicleName.EMAIL)
public class Vehicle extends BaseEntity {
    private static final long serialVersionUID = -5360751385120611439L;
    // TODO name must be url safe
    @NotNull
    @NotEmpty
    @Column(nullable = false)
    private String vehicleName;
    @NotNull
    @ManyToOne(optional = false)
    private Owner owner;

    public Owner getOwner() {
        return this.owner;
    }

    public Vehicle setOwner(final Owner value) {
        this.owner = value;
        return this;
    }

    public String getVehicleName() {
        return this.vehicleName;
    }

    public Vehicle setVehicleName(final String value) {
        this.vehicleName = value;
        return this;
    }

    public static final class QueryByEmailAndVehicleName {
        public static final String NAME = "Vehicle.byEmailAndVehicleName";
        public static final String EMAIL = "email";
        public static final String VEHICLE_NAME = "vehicleName";
    }
}
