package biz.suckow.fuel.business.refueling.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;
import com.google.common.collect.Lists;

@Entity
public class FuelStock extends BaseEntity {
    @OneToMany
    private final List<Refueling> add;

    @OneToMany
    private final List<StockRelease> remove;

    public FuelStock() {
        this.remove = Lists.newArrayList();
        this.add = Lists.newArrayList();
    }

    public void addRefueling(Refueling refueling) {
	this.add.add(refueling);
    }

    public void addStockRelease(StockRelease out) {
	this.remove.add(out);
    }
    
    public List<Refueling> getAdd() {
        return this.add;
    }
    
    public List<StockRelease> getRemove() {
        return this.remove;
    }
}
