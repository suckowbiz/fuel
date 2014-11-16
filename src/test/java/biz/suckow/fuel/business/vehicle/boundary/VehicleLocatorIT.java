package biz.suckow.fuel.business.vehicle.boundary;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.Matchers.*;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class VehicleLocatorIT extends ArquillianBase {
    @Inject
    private EntityManager em;

    @Inject
    private VehicleLocator cut;

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void verifyVehicleFetchingSucceeds() {
        final Owner owner = new Owner().setOwnername("duke");
        this.em.persist(owner);

        final Vehicle vehicle = new Vehicle().setOwner(owner).setVehiclename("duke-bike");
        this.em.persist(vehicle);

        final Vehicle actualResult = this.cut.getVehicle("duke", "duke-bike").get();
        assertThat(actualResult, not(nullValue()));
        assertThat(actualResult.getId(), not(nullValue()));
        assertThat(actualResult.getId(), equalTo(vehicle.getId()));
    }

    @Test
    public void verifyVehicleFetchingFails() {
        final Optional<Vehicle> possibleVehicle = this.cut.getVehicle("duke", "duke-bike");
        assertThat(possibleVehicle.isPresent(), is(false));
    }
}
