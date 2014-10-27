package biz.suckow.fuel.business.refueling.control;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class RefuelingService {
    @PersistenceContext
    private EntityManager em;

    public Vehicle fullTankRefuel(Vehicle vehicle, Double kilometres,
	    Double litres, Double eurosPerLitre, Date date, String memo) {
	Refueling refueling = new Refueling.Builder()
		.eurosPerLitre(eurosPerLitre).litres(litres).memo(memo)
		.date(date).fillUp(true).build();
	em.persist(refueling);

	vehicle.addRefueling(refueling);
	return em.merge(vehicle);
    }

    public Vehicle partialTankRefuel(Vehicle vehicle, Double litres,
	    Double euros, Date date, String memo) {
	Refueling refueling = new Refueling.Builder().litres(litres)
		.eurosPerLitre(euros).date(date).memo(memo).build();
	em.persist(refueling);

	vehicle.addRefueling(refueling);
	return em.merge(vehicle);
    }

    public void fullTankAndStockRefuel(Vehicle vehicle, Double kilometers,
	    Double litresTank, Double litresStock, Double euros, Date date,
	    String memo) {
	Vehicle mergedVehicle = this.fullTankRefuel(vehicle, kilometers,
		litresTank, euros, date, memo);
	this.stockRefuel(mergedVehicle, litresStock, euros, date, null);
    }

    public void stockRefuel(Vehicle vehicle, Double litres, Double euros,
	    Date date, String memo) {
	Refueling stockRefueling = new Refueling.Builder().date(date)
		.litres(litres).memo(memo).build();
	em.persist(stockRefueling);

	vehicle.getFuelStock().addRefueling(stockRefueling);
	em.merge(vehicle);
    }
}
