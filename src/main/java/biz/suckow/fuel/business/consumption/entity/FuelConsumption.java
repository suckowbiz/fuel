package biz.suckow.fuel.business.consumption.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.app.entity.BaseEntity;

@Entity
public class FuelConsumption extends BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
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
