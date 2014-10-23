package biz.suckow.fuel.business.refueling.control;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.refueling.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.user.UserService;
import biz.suckow.fuel.business.user.entity.User;

public class RefuelingService {
    @Inject
    private UserService userService;

    @Inject
    private RefuelingMaths maths;

    @PersistenceContext
    private EntityManager em;

    // 1. from station fills tank
    public void standardRefuel(String username, Double kilometres,
	    Double litres, Double eurosPerLitre, Date date, String memo) {
	Refueling refueling = new Refueling.Builder()
		.eurosPerLitre(eurosPerLitre).litres(litres).memo(memo)
		.date(date).build();
	em.persist(refueling);

	Double litresPerKilometre = null; // maths.computeActualFuelConsumption(username,
					  // kilometres, litres, euros);
	FuelConsumption consumption = new FuelConsumption();
	consumption.setDate(new Date());
	consumption.setLitresPerKilometre(litresPerKilometre);
	em.persist(consumption);

	User user = this.userService.getUser(username);
	user.addRefueling(refueling);
	user.addFuelConsuption(consumption);
	em.merge(user);
    }

    // 2. from station fills tank partially
    public void partialRefuel(String username, Double litres, Double euros,
	    Date date, String memo) {
	Refueling refueling = new Refueling.Builder().litres(litres)
		.eurosPerLitre(euros).date(date).memo(memo).build();
	em.persist(refueling);

	User user = this.userService.getUser(username);
	user.addRefueling(refueling);
	em.merge(user);
    }

    // 3. from station fills tank and stock
    public void overRefuel(String username, Double kilometers,
	    Double litresTank, Double litresStock, Double euros, Date date,
	    String memo) {
	this.standardRefuel(username, kilometers, litresTank, euros, date, memo);
	this.refuelStock(username, litresStock, euros, date, null);
    }

    public void refuelStock(String username, Double litres, Double euros,
	    Date date, String memo) {
	Refueling stockRefueling = new Refueling.Builder().date(date)
		.litres(litres).memo(memo).build();
	em.persist(stockRefueling);

	User user = this.userService.getUser(username);
	user.getFuelStock().addRefueling(stockRefueling);
	em.merge(user);
    }
}
