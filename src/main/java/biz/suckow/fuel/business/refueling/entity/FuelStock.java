package biz.suckow.fuel.business.refueling.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;

@Entity
public class FuelStock extends BaseEntity {
    @OneToMany
    private List<Refueling> in;

    @OneToMany
    private List<StockRelease> out;

    public void addRefueling(Refueling refueling) {
	this.in.add(refueling);
    }

    public void addStockRelease(StockRelease out) {
	this.out.add(out);
    }
}
