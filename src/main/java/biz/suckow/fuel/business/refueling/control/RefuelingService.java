package biz.suckow.fuel.business.refueling.control;

import java.util.Date;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.user.boundary.UserService;
import biz.suckow.fuel.business.user.entity.User;

public class RefuelingService {
    @Inject
    private UserService userService;

    @PersistenceContext
    private EntityManager em;

    public void fullTankRefuel(String username, Double kilometres,
	    Double litres, Double eurosPerLitre, Date date, String memo) {
	Refueling refueling = new Refueling.Builder()
		.eurosPerLitre(eurosPerLitre).litres(litres).memo(memo)
		.date(date).fillUp(true).build();
	em.persist(refueling);

	User user = this.userService.getUser(username);
	user.addRefueling(refueling);
	em.merge(user);
    }

    public void partialTankRefuel(String username, Double litres, Double euros,
	    Date date, String memo) {
	Refueling refueling = new Refueling.Builder().litres(litres)
		.eurosPerLitre(euros).date(date).memo(memo).build();
	em.persist(refueling);

	User user = this.userService.getUser(username);
	user.addRefueling(refueling);
	em.merge(user);
    }

    public void fullTankAndStockRefuel(String username, Double kilometers,
	    Double litresTank, Double litresStock, Double euros, Date date,
	    String memo) {
	this.fullTankRefuel(username, kilometers, litresTank, euros, date, memo);
	this.stockRefuel(username, litresStock, euros, date, null);
    }

    public void stockRefuel(String username, Double litres, Double euros,
	    Date date, String memo) {
	Refueling stockRefueling = new Refueling.Builder().date(date)
		.litres(litres).memo(memo).build();
	em.persist(stockRefueling);

	User user = this.userService.getUser(username);
	user.getFuelStock().addRefueling(stockRefueling);
	em.merge(user);
    }
}
