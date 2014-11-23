package biz.suckow.fuel.business.vehicle.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import javax.inject.Inject;

import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class VehicleLocatorIT extends ArquillianBase {
    @Inject
    private VehicleLocator cut;

    @Test
    @Transactional(TransactionMode.ROLLBACK)
    public void mustFetchExistingVehicle() {
        final Vehicle dukeCar = this.getCreatedAndPersistedDukeCar();
        final Vehicle actualResult = this.cut.getVehicle(dukeCar.getOwner().getOwnername(), dukeCar.getVehiclename())
                .get();
        assertThat(actualResult).isNotNull();
        assertThat(actualResult.getId()).isEqualTo(dukeCar.getId());
    }

    @Test
    public void mustNotFetchNonExistingVehicle() {
        final Optional<Vehicle> possibleVehicle = this.cut.getVehicle("duke", "duke-bike");
        assertThat(possibleVehicle).isAbsent();
    }
}
