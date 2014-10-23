package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
public class Refueling {
    @Id
    @GeneratedValue
    private Long id;

    public static final class Builder {
	private Refueling refueling = new Refueling();

	public Builder() {
	    this.refueling.setDate(new Date());
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

	public Refueling build() {
	    return this.refueling;
	}
    }

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @DecimalMin(value = "0.009", inclusive = true)
    @NotNull
    @Column
    private Double eurosPerLitre;

    @Min(value = 1)
    @NotNull
    @Column
    private Double litres;

    @Column
    private String memo;

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

    public Long getId() {
	return id;
    }
}
