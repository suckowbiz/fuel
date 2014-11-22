package biz.suckow.fuel.business.vehicle.entity;

import static org.assertj.core.api.Assertions.assertThat;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.entity.Owner;

public class VehicleIT extends ArquillianBase {
    @Inject
    private EntityManager em;

    @Test
    public void mustNotPersistDuplicateVehicle() {
        final Owner owner = new Owner();
        owner.setOwnername("duke");
        this.em.persist(owner);

        Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehiclename("duke-car");
        this.em.persist(vehicle);

        try {
            vehicle = new Vehicle();
            vehicle.setOwner(owner);
            vehicle.setVehiclename("duke-car");
            this.em.persist(vehicle);
            this.em.flush();
        } catch (final Throwable t) {
            assertThat(t).hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }
}
