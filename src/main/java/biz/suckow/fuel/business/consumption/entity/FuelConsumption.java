package biz.suckow.fuel.business.consumption.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class FuelConsumption {
    @Column(nullable = false)
    private Date date;

    @Column(nullable = false)
    private Double litresPerKilometre;

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Double getLitresPerKilometre() {
	return litresPerKilometre;
    }

    public void setLitresPerKilometre(Double litresPerKilometre) {
	this.litresPerKilometre = litresPerKilometre;
    }
}
