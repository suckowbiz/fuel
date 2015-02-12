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
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"vehicleName", "owner_id"}))
@NamedQueries({@NamedQuery(name = Vehicle.BY_EMAIL_AND_VEHICLE_NAME, query = "SELECT v FROM Vehicle v "
        + " WHERE v.vehicleName = :vehicleName AND v.owner.email = :email"),
        @NamedQuery(name = Vehicle.BY_EMAIL, query = "SELECT v FROM Vehicle v WHERE v.owner.email = :email")})
public class Vehicle extends BaseEntity {
    private static final String PREFIX = "biz.suckow.fuelservice.business.vehicle.entity.";

    private static final long serialVersionUID = -5360751385120611439L;

    public static final String BY_EMAIL_AND_VEHICLE_NAME = PREFIX + "byEmailAndVehicleName";

    public static final String BY_EMAIL = PREFIX + "byEmail";

    @Column(nullable = false)
    private String vehicleName;

    @ManyToOne(optional = false)
    private Owner owner;

    @OrderBy("id DESC")
    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.REMOVE)
    private Set<Refuelling> refuellings = new HashSet<>();

    public Set<Refuelling> getRefuellings() {
        return refuellings;
    }

    public void setRefuellings(Set<Refuelling> refuellings) {
        this.refuellings = refuellings;
    }

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
}
