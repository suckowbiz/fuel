package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class StockRelease {
    private Double litres;
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}
