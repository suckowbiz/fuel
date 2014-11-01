package biz.suckow.fuel.business.vehicle.boundary;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class VehicleLocator {
    @PersistenceContext
    private EntityManager em;

    // TODO test
    public Vehicle getVehicle(String ownername, String vehiclename) {
	Vehicle result = (Vehicle) this.em
		.createNamedQuery(Vehicle.QueryByOwnerAndVehicle.NAME)
		.setParameter(Vehicle.QueryByOwnerAndVehicle.PARAM_OWNERNAME_NAME,
			ownername)
		.setParameter(Vehicle.QueryByOwnerAndVehicle.PARAM_VEHICLENAME_NAME,
			vehiclename).getSingleResult();
	return result;
    }
}
