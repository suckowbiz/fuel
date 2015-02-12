package biz.suckow.fuelservice.business.refuelling.entity;

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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Entity
public class StockRelease extends BaseEntity {
    private static final long serialVersionUID = 7021516650764717192L;

    @Column(nullable = false)
    private Double litres;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateReleased;

    public Double getLitres() {
        return this.litres;
    }

    public StockRelease setLitres(final Double litres) {
        this.litres = litres;
        return this;
    }

    public Date getDateReleased() {
        return this.dateReleased;
    }

    public StockRelease setDateReleased(final Date dateReleased) {
        this.dateReleased = dateReleased;
        return this;
    }
}
