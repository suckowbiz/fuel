package biz.suckow.fuel.business.vehicle.boundary;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class VehicleLocator {
    @PersistenceContext
    private EntityManager em;

    // TODO test
    public List<Vehicle> getVehiclesMissingConsumptionOldestFirst() {
	@SuppressWarnings("unchecked")
	List<Vehicle> result = this.em.createNamedQuery(
		Vehicle.BY_MISSING_CONSUMPTION_OLDEST_FIRST).getResultList();
	return result;
    }

    // TODO test
    public Vehicle getVehicle(String username, String vehiclename) {
	Vehicle result = (Vehicle) this.em
		.createNamedQuery(Vehicle.ByUserAndVehicle.NAME)
		.setParameter(Vehicle.ByUserAndVehicle.PARAM_USERNAME_NAME,
			username)
		.setParameter(Vehicle.ByUserAndVehicle.PARAM_VEHICLENAME_NAME,
			vehiclename).getSingleResult();
	return result;
    }
}
