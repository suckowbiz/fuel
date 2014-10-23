package biz.suckow.fuel.business.refueling.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class RefuelingMeta {
    public Date date;
    public String memo;
    public Double kilometers;
    public Double eurosPerLitre;
    public Double litresToTank;
    public Double litresToStock;
    public Double litresReleasedFromStock;
}
