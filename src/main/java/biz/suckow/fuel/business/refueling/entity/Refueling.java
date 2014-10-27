package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
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

@NamedQuery(name = Refueling.ByExistingConsumptionForDateNewestFirst.NAME, query = "FROM Refueling r WHERE r.isFillUp = true "
	+ "AND r.consumption IS NOT NULL AND r.date < :date ORDER BY r.date DESC ") })
public class Refueling extends BaseEntity {
    public static final class ByExistingConsumptionForDateNewestFirst {
	public static final String NAME = "Refueling.byExistingConsumptionForDate";
	public static final String PARAM_NAME = "Refueling.byExistingConsumptionForDateParam";
    }

    // TODO add multi vehicle-ancy
    public static final class Builder {
	private Refueling refueling = new Refueling();

	/**
	 * Standard constructor to initiate optional/ default values.
	 */
	public Builder() {
	    this.refueling.setDate(new Date());
	    this.refueling.setIsFillUp(false);
	}

	public Builder eurosPerLitre(Double value) {
	    this.refueling.setEurosPerLitre(value);
	    return this;
	}

	public Builder litres(Double value) {
	    this.refueling.setLitres(value);
	    return this;
	}

	public Builder memo(String memo) {
	    this.refueling.setMemo(memo);
	    return this;
	}

	public Builder date(Date date) {
	    this.refueling.setDate(date);
	    return this;
	}

	public Builder fillUp(boolean value) {
	    this.refueling.setIsFillUp(value);
	    return this;
	}

	public Builder vehicle(Vehicle vehicle) {
	    this.refueling.setVehicle(vehicle);
	    return this;
	}

	public Refueling build() {
	    return this.refueling;
	}
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    @DecimalMin(value = "0.009", inclusive = true)
    @NotNull
    @Column(nullable = false)
    private Double eurosPerLitre;

    @Min(value = 1)
    @NotNull
    @Column(nullable = false)
    private Double litres;

    @Column
    private String memo;

    @Column(nullable = false)
    private Boolean isFillUp;

    @Column
    private FuelConsumption consumption;

    @Column(nullable = false)
    private Vehicle vehicle;

    public Vehicle getVehicle() {
	return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
	this.vehicle = vehicle;
    }

    public FuelConsumption getConsumption() {
	return consumption;
    }

    public void setConsumption(FuelConsumption consumption) {
	this.consumption = consumption;
    }

    @Column
    public Boolean getIsFillUp() {
	return isFillUp;
    }

    public void setIsFillUp(Boolean isFillUp) {
	this.isFillUp = isFillUp;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Double getEurosPerLitre() {
	return eurosPerLitre;
    }

    public void setEurosPerLitre(Double eurosPerLitre) {
	this.eurosPerLitre = eurosPerLitre;
    }

    public Double getLitres() {
	return litres;
    }

    public void setLitres(Double litres) {
	this.litres = litres;
    }

    public String getMemo() {
	return memo;
    }

    public void setMemo(String memo) {
	this.memo = memo;
    }

}
