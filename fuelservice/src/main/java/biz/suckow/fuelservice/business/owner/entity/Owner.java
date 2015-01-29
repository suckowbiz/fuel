package biz.suckow.fuelservice.business.owner.entity;

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

import biz.suckow.fuelservice.business.BaseEntity;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * An owner is a representation of an application user that is uniquely identified by email.
 */
@Entity
@NamedQuery(name = Owner.QueryByEmailCaseIgnore.NAME, query = "SELECT o FROM Owner o "
        + "WHERE LOWER(o.email) = LOWER(:" + Owner.QueryByEmailCaseIgnore.EMAIL + ")")
public class Owner extends BaseEntity {
    private static final long serialVersionUID = -2640121939957877859L;
    @NotNull
    @Column(nullable = false)
    private Role role;
    @OneToMany(mappedBy = "owner")
    private Set<Vehicle> vehicles;
    @NotNull
    @Column(unique = true, nullable = false)
    private String email;

    /**
     * The password is expected to be hashed by frontend. Password are stored "as is".
     */
    @NotNull
    @Column(nullable = false)
    private String password;

    public Owner() {
        this.role = Role.OWNER;
        this.vehicles = new HashSet<>();
    }

    public String getPassword() {
        return password;
    }

    public Owner setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return this.email;
    }

    public Owner setEmail(String value) {
        this.email = value;
        return this;
    }

    public Set<Vehicle> getVehicles() {
        return this.vehicles;
    }

    public Role getRole() {
        return role;
    }

    public Owner setRole(Role role) {
        this.role = role;
        return this;
    }

    public static final class QueryByEmailCaseIgnore {
        public static final String NAME = "Owner.byEmail";
        public static final String EMAIL = "ownername";
    }
}
