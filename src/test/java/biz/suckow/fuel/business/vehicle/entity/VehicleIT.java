package biz.suckow.fuel.business.vehicle.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.TestHelper;

public class VehicleIT extends ArquillianBase {
    @Test
    public void mustNotPersistDuplicateVehicle() {
        final Vehicle dukeCar = TestHelper.getCreatedAndPersistedDukeCar(this.em);

        try {
            final Vehicle vehicle = new Vehicle();
            vehicle.setOwner(dukeCar.getOwner());
            vehicle.setVehiclename(dukeCar.getVehiclename());
            this.em.persist(vehicle);
            this.em.flush();
        } catch (final Throwable t) {
            assertThat(t).hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }
}
