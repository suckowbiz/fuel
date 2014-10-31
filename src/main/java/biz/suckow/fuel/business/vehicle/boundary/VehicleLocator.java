package biz.suckow.fuel.business.vehicle.boundary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class VehicleLocator {
    @PersistenceContext
    private EntityManager em;

    // TODO test
    public Vehicle getVehicle(String username, String vehiclename) {
	Vehicle result = (Vehicle) this.em
		.createNamedQuery(Vehicle.QueryByUserAndVehicle.NAME)
		.setParameter(Vehicle.QueryByUserAndVehicle.PARAM_USERNAME_NAME,
			username)
		.setParameter(Vehicle.QueryByUserAndVehicle.PARAM_VEHICLENAME_NAME,
			vehiclename).getSingleResult();
	return result;
    }
}
