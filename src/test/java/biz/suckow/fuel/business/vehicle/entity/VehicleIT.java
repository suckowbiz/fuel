package biz.suckow.fuel.business.vehicle.entity;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.entity.Owner;

public class VehicleIT extends ArquillianBase {
    @Inject
    private EntityManager em;

    @Test(expectedExceptions = RuntimeException.class,
            expectedExceptionsMessageRegExp = "Unable to commit the transaction.")
    public void mustNotPersistNonUniqueVehiclename() throws Throwable {
        final Owner owner = new Owner();
        owner.setOwnername("duke");
        this.em.persist(owner);

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehiclename("duke-car");
        this.em.persist(vehicle);

        vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehiclename("duke-car");
        this.em.persist(vehicle);
    }
}
