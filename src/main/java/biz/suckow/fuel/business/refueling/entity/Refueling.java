package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Refueling {

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Column
    private Double eurosPerLitre;

    @Column
    private Double litres;

    @Column
    private String memo;
}
