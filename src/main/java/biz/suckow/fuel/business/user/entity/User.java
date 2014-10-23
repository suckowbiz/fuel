package biz.suckow.fuel.business.user.entity;

import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.refueling.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.FuelStock;
import biz.suckow.fuel.business.refueling.entity.Refueling;

@Entity
public class User {
    @OneToMany
    private List<Refueling> refuelings = Collections.emptyList();
    private FuelStock fuelStock;
    private List<FuelConsumption> fuelConsumptions = Collections.emptyList();

    public void addFuelConsuption(FuelConsumption consumption) {
	this.fuelConsumptions.add(consumption);
    }

    public void addRefueling(Refueling refueling) {
	this.refuelings.add(refueling);
    }

    public FuelStock getFuelStock() {
	return fuelStock;
    }

}
