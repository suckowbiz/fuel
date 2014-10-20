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
    public void standardRefuel(String username, String kilometres,
	    String litres, String euros, String memo) {
	Refueling refueling = new Refueling.Builder().eurosPerLitre(euros)
		.litres(litres).memo(memo).build();
	em.persist(refueling);

	Double litresPerKilometre = maths.computeActualFuelConsumption(
		username, kilometres, litres, euros);
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
    public void partialRefuel(String username, String litres, String euros,
	    String memo) {

    }

    // 3. from station fills tank and stock
    public void overRefuel(String username, String kilometers,
	    String litresTank, String litresStock, String euros, String memo) {

    }

    // 4. from station fill stock
    public void refuelStock(String username, String litres, String euros,
	    String memo) {

    }

    // 5. from stock fills tank
    public void fromStockFullRefuel(String username, String litres,
	    String kilometres, String memo) {

    }

    // 6. from stock fills tank partially
    public void fromStockPartialRefuel(String username, String litres,
	    String memo) {

    }
}
