package biz.suckow.fuel.business.refueling.entity;

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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import biz.suckow.fuel.business.app.entity.BaseEntity;

@Entity
public class StockAddition extends BaseEntity {
    private static final long serialVersionUID = 6350184958523817787L;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateAdded;

    @NotNull
    @DecimalMin(value = "0.009", inclusive = true)
    @Column(nullable = false)
    private Double eurosPerLitre;

    @Min(value = 1)
    @NotNull
    @Column(nullable = false)
    private Double litres;

    @Column
    private String memo;

    public Date getDateRefueled() {
        return this.dateAdded;
    }

    public StockAddition setDateAdded(final Date date) {
        this.dateAdded = date;
        return this;
    }

    public Double getEurosPerLitre() {
        return this.eurosPerLitre;
    }

    public StockAddition setEurosPerLitre(final Double eurosPerLitre) {
        this.eurosPerLitre = eurosPerLitre;
        return this;
    }

    public Double getLitres() {
        return this.litres;
    }

    public StockAddition setLitres(final Double litres) {
        this.litres = litres;
        return this;
    }

    public String getMemo() {
        return this.memo;
    }

    public StockAddition setMemo(final String memo) {
        this.memo = memo;
        return this;
    }

}
