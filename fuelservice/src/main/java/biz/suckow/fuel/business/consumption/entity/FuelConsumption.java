package biz.suckow.fuel.business.consumption.entity;

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

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@Entity
public class FuelConsumption extends BaseEntity {
    private static final long serialVersionUID = -6145466237775398192L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateComputed;

    @Column(nullable = false)
    private Double litresPerKilometre;

    @NotNull
    @OneToOne(optional = false)
    private Vehicle vehicle;

    public Date getDateComputed() {
	return this.dateComputed;
    }

    public Vehicle getVehicle() {
	return this.vehicle;
    }

    public FuelConsumption setVehicle(final Vehicle vehicle) {
	this.vehicle = vehicle;
	return this;
    }

    public FuelConsumption setDateComputed(final Date dateComputed) {
	this.dateComputed = dateComputed;
	return this;
    }

    public Double getLitresPerKilometre() {
	return this.litresPerKilometre;
    }

    public FuelConsumption setLitresPerKilometre(final Double litresPerKilometre) {
	this.litresPerKilometre = litresPerKilometre;
	return this;
    }
}