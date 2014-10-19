package biz.suckow.fuel.business.user.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.refueling.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;

@Entity
public class User {
    @OneToMany
    private List<Refueling> refuelings;
    private FuelStock fuelStock;
    private List<FuelConsumption> fuelConsumptions;
}
