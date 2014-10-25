package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.user.entity.User;

@Entity
public class Refueling extends BaseEntity {
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

	public Builder user(User user) {
	    this.refueling.setUser(user);
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

    @Column
    private Boolean isFillUp;

    @Column
    private FuelConsumption consumption;

    @Column(nullable = false)
    private User user;

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
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
