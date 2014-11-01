package biz.suckow.fuel.business.consumption.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.app.entity.BaseEntity;

@Entity
public class FuelConsumption extends BaseEntity {
    private static final long serialVersionUID = -6145466237775398192L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateComputed;

    @Column(nullable = false)
    private Double litresPerKilometre;

    public Date getDateComputed() {
	return dateComputed;
    }

    public void setDateComputed(Date dateComputed) {
	this.dateComputed = dateComputed;
    }

    public Double getLitresPerKilometre() {
	return litresPerKilometre;
    }

    public void setLitresPerKilometre(Double litresPerKilometre) {
	this.litresPerKilometre = litresPerKilometre;
    }
}
