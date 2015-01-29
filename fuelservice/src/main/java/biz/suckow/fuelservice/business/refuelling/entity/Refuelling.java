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
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.persistence.*;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NamedQueries({
        @NamedQuery(name = Refuelling.FIND_PARTIALS_BY_VEHICLE_AND_DATE_BETWEEN, query = "SELECT r FROM Refuelling r WHERE r.isFillUp = false AND r.dateRefueled > :left "
                + "AND r.dateRefueled < :right AND r.vehicle = :vehicle"),
        @NamedQuery(name = Refuelling.FIND_BY_FILLED_UP_AND_DATE_BEFORE, query = "SELECT r FROM Refuelling r WHERE r.isFillUp = true "
                + "AND r.dateRefueled < :right ORDER BY r.dateRefueled DESC ")})
public class Refuelling extends BaseEntity {
    private static final long serialVersionUID = 9175526663957115977L;
    private static final String PREFIX = "biz.suckow.fuelservice.business.refuelling.entity.";
    public static final String FIND_PARTIALS_BY_VEHICLE_AND_DATE_BETWEEN = Refuelling.PREFIX
            + "findByVehicleAndRefuelDate";
    public static final String FIND_BY_FILLED_UP_AND_DATE_BEFORE = Refuelling.PREFIX + "findByFilledUpAndDateBefore";
    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateRefueled;
    @NotNull
    @DecimalMin(value = "0.009", inclusive = true)
    @Column(nullable = false)
    private Double eurosPerLitre;
    @DecimalMin(value = "0.00", inclusive = false)
    private Double kilometre;
    @Min(value = 1)
    @NotNull
    @Column(nullable = false)
    private Double litres;
    @Column
    private String memo;
    @Column(nullable = false)
    private Boolean isFillUp;
    @ManyToOne
    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public Refuelling setVehicle(final Vehicle vehicle) {
        this.vehicle = vehicle;
        return this;
    }

    public Boolean getIsFillUp() {
        return this.isFillUp;
    }

    public void setIsFillUp(final Boolean isFillUp) {
        this.isFillUp = isFillUp;
    }

    public Date getDateRefueled() {
        return this.dateRefueled;
    }

    public Refuelling setDateRefueled(final Date dateRefueled) {
        this.dateRefueled = dateRefueled;
        return this;
    }

    public Double getEurosPerLitre() {
        return this.eurosPerLitre;
    }

    public void setEurosPerLitre(final Double eurosPerLitre) {
        this.eurosPerLitre = eurosPerLitre;
    }

    public Double getLitres() {
        return this.litres;
    }

    public void setLitres(final Double litres) {
        this.litres = litres;
    }

    public String getMemo() {
        return this.memo;
    }

    public void setMemo(final String memo) {
        this.memo = memo;
    }

    public Double getKilometre() {
        return this.kilometre;
    }

    public void setKilometre(final Double kilometre) {
        this.kilometre = kilometre;
    }

    public static final class Builder {
        private final Refuelling refuelling;

        /**
         * Standard constructor to initiate optional/ default values.
         */
        public Builder() {
            this.refuelling = new Refuelling();
            this.refuelling.setDateRefueled(new Date());
            this.refuelling.setIsFillUp(false);
        }

        public Builder eurosPerLitre(final Double value) {
            this.refuelling.setEurosPerLitre(value);
            return this;
        }

        public Builder litres(final Double value) {
            this.refuelling.setLitres(value);
            return this;
        }

        public Builder kilometre(final Double value) {
            this.refuelling.setKilometre(value);
            return this;
        }

        public Builder memo(final String memo) {
            this.refuelling.setMemo(memo);
            return this;
        }

        public Builder dateRefueled(final Date date) {
            this.refuelling.setDateRefueled(date);
            return this;
        }

        public Builder fillUp(final boolean value) {
            this.refuelling.setIsFillUp(value);
            return this;
        }

        public Builder vehicle(final Vehicle vehicle) {
            this.refuelling.setVehicle(vehicle);
            return this;
        }

        public Refuelling build() {
            if (this.refuelling.getIsFillUp() && this.refuelling.getKilometre() == null)
                throw new IllegalArgumentException("Combination of filled up refuelling with missing kilometre.");
            return this.refuelling;
        }
    }

}
