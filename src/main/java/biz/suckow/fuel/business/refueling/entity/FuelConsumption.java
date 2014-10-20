package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

public class FuelConsumption {
    private Date date;
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
