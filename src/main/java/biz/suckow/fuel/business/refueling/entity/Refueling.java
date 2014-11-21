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
package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@Entity
@NamedQueries({
        @NamedQuery(name = Refueling.QueryPartialRefuelingsBetween.NAME,
                query = "SELECT r FROM Refueling r WHERE r.isFillUp = false AND r.dateRefueled > :"
                        + Refueling.QueryPartialRefuelingsBetween.PARAM_NAME_LEFT + " AND r.dateRefueled < :"
                        + Refueling.QueryPartialRefuelingsBetween.PARAM_NAME_RIGHT + " AND r.vehicle = :"
                        + Refueling.QueryPartialRefuelingsBetween.PARAM_NAME_VEHICLE),
        @NamedQuery(name = Refueling.QueryLatestByFilledUpBeforeDate.NAME,
                query = "SELECT r FROM Refueling r WHERE r.isFillUp = true " + "AND r.dateRefueled < :"
                            + Refueling.QueryLatestByFilledUpBeforeDate.PARAM_NAME + " ORDER BY r.dateRefueled DESC "),
        @NamedQuery(
                name = Refueling.BY_FILLED_UP_AND_MISSING_CONSUMPTION_OLDEST_FIRST,
                query = "SELECT r FROM Refueling r WHERE r.isFillUp = true AND r.consumption IS NULL ORDER BY r.dateRefueled DESC") })
public class Refueling extends BaseEntity {
    private static final long serialVersionUID = 9175526663957115977L;

    public static final String BY_FILLED_UP_AND_MISSING_CONSUMPTION_OLDEST_FIRST = "Vehicle.byMissingConsumptionOldestFirst";

    public static final class QueryPartialRefuelingsBetween {
        public static final String NAME = "Refueling.partialRefuelingsBetween";
        public static final String PARAM_NAME_LEFT = "dateLeft";
        public static final String PARAM_NAME_RIGHT = "dateRight";
        public static final String PARAM_NAME_VEHICLE = "vehicle";
    }

    public static final class QueryLatestByFilledUpBeforeDate {
        public static final String NAME = "Refueling.latestByFilledUpBeforeDate";
        public static final String PARAM_NAME = "dateParam";
    }

    public static final class Builder {
        private final Refueling refueling = new Refueling();

        /**
         * Standard constructor to initiate optional/ default values.
         */
        public Builder() {
            this.refueling.setDateRefueled(new Date());
            this.refueling.setIsFillUp(false);
        }

        public Builder eurosPerLitre(final Double value) {
            this.refueling.setEurosPerLitre(value);
            return this;
        }

        public Builder litres(final Double value) {
            this.refueling.setLitres(value);
            return this;
        }

        public Builder kilometre(final Double value) {
            this.refueling.setKilometre(value);
            return this;
        }

        public Builder memo(final String memo) {
            this.refueling.setMemo(memo);
            return this;
        }

        public Builder dateRefueled(final Date date) {
            this.refueling.setDateRefueled(date);
            return this;
        }

        public Builder fillUp(final boolean value) {
            this.refueling.setIsFillUp(value);
            return this;
        }

        public Builder vehicle(final Vehicle vehicle) {
            this.refueling.setVehicle(vehicle);
            return this;
        }

        public Refueling build() {
            if (this.refueling.getIsFillUp() && this.refueling.getKilometre() == null) {
                throw new IllegalArgumentException("Combination of filled up refueling with missing kilometre.");
            }
            return this.refueling;
        }
    }

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

    @OneToOne
    private FuelConsumption consumption;

    @ManyToOne
    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return this.vehicle;
    }

    public void setVehicle(final Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public FuelConsumption getConsumption() {
        return this.consumption;
    }

    public void setConsumption(final FuelConsumption consumption) {
        this.consumption = consumption;
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

    public void setDateRefueled(final Date dateRefueled) {
        this.dateRefueled = dateRefueled;
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

    public void setKilometre(final Double kilometre) {
        this.kilometre = kilometre;
    }

    public Double getKilometre() {
        return this.kilometre;
    }

}
