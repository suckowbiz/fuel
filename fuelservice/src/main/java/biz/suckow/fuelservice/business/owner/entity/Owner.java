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

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@NamedQuery(name = Owner.BY_EMAIL_CASE_IGNORE, query = "SELECT o FROM Owner o WHERE LOWER(o.email) = LOWER(:email)")
public class Owner extends BaseEntity {
    private static final long serialVersionUID = -2640121939957877859L;

    private static final String PREFIX = "biz.suckow.fuelservice.business.owner.entity.";

    public static final String BY_EMAIL_CASE_IGNORE = PREFIX + "byEmail";

    @Column(nullable = false)
    @ElementCollection(fetch = FetchType.EAGER)
    private Set<Role> roles;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.REMOVE)
    private Set<Vehicle> vehicles;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private Boolean isLoggedOut = true;

    /**
     * The password is expected to be hashed by frontend. Password are stored "as is".
     */
    @Column(nullable = false)
    private String password;

    public Owner() {
        this.roles = new HashSet<>();
        this.vehicles = new HashSet<>();
    }

    public Boolean getIsLoggedOut() {
        return isLoggedOut;
    }

    public void setIsLoggedOut(Boolean isLoggedOn) {
        this.isLoggedOut = isLoggedOn;
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

    public Owner setVehicles(Set<Vehicle> vehicles) {
        this.vehicles = vehicles;
        return this;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public Owner setRoles(Set<Role> roles) {
        this.roles = roles;
        return this;
    }

    public Owner addRole(Role value) {
        this.roles.add(value);
        return this;
    }

    public Owner addVehicle(Vehicle vehicle) {
        this.vehicles.add(vehicle);
        return this;
    }
}
