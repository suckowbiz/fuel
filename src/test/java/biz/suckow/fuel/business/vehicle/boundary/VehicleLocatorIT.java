package biz.suckow.fuel.business.vehicle.boundary;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class VehicleLocatorIT extends ArquillianBase {
    @Inject
    private EntityManager em;

    @Inject
    private VehicleLocator cut;

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void stest() {
        final Owner owner = new Owner().setOwnername("duke");
        this.em.persist(owner);

        final Vehicle vehicle = new Vehicle().setOwner(owner).setVehiclename(
                "duke-bike");
        this.em.persist(vehicle);

        final Vehicle actualResult = this.cut.getVehicle("duke", "duke-bike");
        MatcherAssert.assertThat(actualResult,
                Matchers.not(Matchers.nullValue()));
        MatcherAssert.assertThat(actualResult.getId(),
                Matchers.not(Matchers.nullValue()));
        MatcherAssert.assertThat(actualResult.getId(),
                Matchers.equalTo(vehicle.getId()));
    }
}
