package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.app.entity.BaseEntity;

@Entity
public class StockRelease extends BaseEntity {
    private static final long serialVersionUID = 7021516650764717192L;

    @Column(nullable = false)
    private Double litres;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date dateReleased;

    public Double getLitres() {
	return litres;
    }

    public void setLitres(Double litres) {
	this.litres = litres;
    }

    public Date getDateReleased() {
	return dateReleased;
    }

    public void setDateReleased(Date dateReleased) {
	this.dateReleased = dateReleased;
    }
}
