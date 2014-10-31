package biz.suckow.fuel.business.consumption.boundary;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.consumption.control.FuelConsumptionMaths;
import biz.suckow.fuel.business.consumption.control.RefuelingLocator;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@Startup
@Singleton
public class FuelConsumptionScheduler {
    @Inject
    private RefuelingLocator locator;

    @Inject
    private FuelConsumptionMaths maths;

    @Inject
    private Logger logger;

    @PersistenceContext
    private EntityManager em;

    AtomicBoolean isRunning = new AtomicBoolean(false);

    // TODO test
    @Schedule(hour = "*", minute = "*", second = "*/30", persistent = false)
    public void timer() {
	if (this.isRunning.get()) {
	    this.logger.log(Level.INFO,
		    "Returning since previous invocation still is running.");
	    return;
	}
	this.isRunning.set(true);
	List<Refueling> refuelings = Collections.emptyList();
	try {
	    refuelings = this.locator
		    .getRefuelingsMissingConsumptionOldestFirst();
	    for (Refueling refueling : refuelings) {
		Double result = this.maths.calculate(refueling);

		FuelConsumption consumption = new FuelConsumption();
		consumption.setDateComputed(refueling.getDateRefueled());
		consumption.setLitresPerKilometre(result);
		em.persist(consumption);

		refueling.setConsumption(consumption);
		em.merge(refueling);

		Vehicle vehicle = refueling.getVehicle();
		vehicle.addFuelConsuption(consumption);
		em.merge(vehicle);
	    }
	} catch (Exception e) {
	    this.logger.log(Level.SEVERE, "Fuel consumption timer crashed.", e);
	} finally {
	    this.logger.log(Level.INFO,
		    "Fuel consumption timer completed for {0} refuelings.",
		    refuelings);
	    this.isRunning.set(false);
	}
    }
}
