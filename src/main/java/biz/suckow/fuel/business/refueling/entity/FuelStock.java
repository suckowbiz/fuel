package biz.suckow.fuel.business.refueling.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;

import com.google.common.collect.Lists;

@Entity
public class FuelStock extends BaseEntity {
    private static final long serialVersionUID = 2386152541780890783L;

    @OneToMany
    private List<Refueling> refuelings;

    @OneToMany
    private List<StockRelease> stockReleases;

    public FuelStock() {
	this.stockReleases = Lists.newArrayList();
	this.refuelings = Lists.newArrayList();
    }

    public void addRefueling(Refueling refueling) {
	this.refuelings.add(refueling);
    }

    public void addStockRelease(StockRelease out) {
	this.stockReleases.add(out);
    }

    public List<Refueling> getRefuelings() {
	return refuelings;
    }

    public List<StockRelease> getStockReleases() {
	return stockReleases;
    }

}
