package biz.suckow.fuel.business.refueling.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class FuelStock {
    @OneToMany
    private List<Refueling> in;
    @OneToMany
    private List<StockRelease> out;
}
