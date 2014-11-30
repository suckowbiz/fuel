package biz.suckow.fuel.business.owner.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@Entity
@NamedQuery(name = Owner.QueryByOwnernameIgnoreCase.NAME, query = "SELECT o FROM Owner o "
	+ "WHERE LOWER(o.ownerName) = LOWER(:" + Owner.QueryByOwnernameIgnoreCase.OWNERNAME + ")")
public class Owner extends BaseEntity {
    private static final long serialVersionUID = -2640121939957877859L;

    public static final class QueryByOwnernameIgnoreCase {
	public static final String NAME = "Owner.byOwnername";
	public static final String OWNERNAME = "ownername";
    }

    @OneToMany(mappedBy = "owner")
    private Set<Vehicle> vehicles;

    @Column(unique = true, nullable = false)
    private String ownerName;

    public Owner() {
	this.vehicles = new HashSet<>();
    }

    public String getOwnername() {
	return this.ownerName;
    }

    public Owner setOwnername(final String ownername) {
	this.ownerName = ownername;
	return this;
    }

    public Set<Vehicle> getVehicles() {
	return this.vehicles;
    }

}
