package biz.suckow.fuel.business.refueling.boundary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.guava.api.Assertions.assertThat;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

import com.google.common.base.Optional;

public class RefuelingLocatorIT extends ArquillianBase {
    @Inject
    private RefuelingLocator cut;

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void mustReturnEmptyForNonExistingFilledUps() {
        final Vehicle dukeCar = this.getCreatedAndPersistedDukeCar();

        final Date march = this.getMonth(2);

        final Refueling refuelingMarchPartial = this.createRefueling(dukeCar, march, false);
        this.em.persist(refuelingMarchPartial);

        final List<Refueling> actualResult = this.cut.getFilledUpAndMissingConsumptionOldestFirst();
        assertThat(actualResult).isEmpty();
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void mustFetchFilledUpWithMissingConsumptionOrdered() {
        final Vehicle dukeCar = this.getCreatedAndPersistedDukeCar();

        final Date january = this.getMonth(0);
        final Date february = this.getMonth(1);
        final Date march = this.getMonth(2);

        Refueling refueling = this.createRefueling(dukeCar, january, true);
        this.em.persist(refueling);

        refueling = this.createRefueling(dukeCar, february, true);
        this.em.persist(refueling);

        refueling = this.createRefueling(dukeCar, march, true);
        this.em.persist(refueling);

        final Refueling refuelingMarchPartial = this.createRefueling(dukeCar, march, false);
        this.em.persist(refuelingMarchPartial);

        final List<Refueling> actualResult = this.cut.getFilledUpAndMissingConsumptionOldestFirst();
        assertThat(actualResult).hasSize(3);
        assertThat(actualResult.get(0).getDateRefueled()).isEqualTo(march);
        assertThat(actualResult.get(1).getDateRefueled()).isEqualTo(february);
        assertThat(actualResult.get(2).getDateRefueled()).isEqualTo(january);
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void mustFetchPartialsBetween() {
        final Vehicle dukeCar = this.getCreatedAndPersistedDukeCar();

        final Vehicle oakCar = new Vehicle().setOwner(dukeCar.getOwner()).setVehiclename("oak-car");
        this.em.persist(oakCar);

        final Date january = this.getMonth(0);
        final Date february = this.getMonth(1);
        final Date march = this.getMonth(2);
        final Date april = this.getMonth(3);

        final Refueling partialFebruaryDuke = this.createRefueling(dukeCar, february, false);
        this.em.persist(partialFebruaryDuke);

        final Refueling fillUpFebruaryDuke = this.createRefueling(dukeCar, february, true);
        this.em.persist(fillUpFebruaryDuke);

        final Refueling partialFebruaryOak = this.createRefueling(oakCar, february, false);
        this.em.persist(partialFebruaryOak);

        final Refueling partialMarchDuke = this.createRefueling(dukeCar, march, false);
        this.em.persist(partialMarchDuke);

        final Refueling fullMarch = this.createRefueling(dukeCar, march, true);
        this.em.persist(fullMarch);

        final Refueling partialAprilDuke = this.createRefueling(dukeCar, april, false);
        this.em.persist(partialAprilDuke);

        final List<Refueling> actualRefuelings = this.cut.getPartialRefuelingsBetween(january, april, dukeCar);
        assertThat(actualRefuelings).hasSize(2).containsOnly(partialFebruaryDuke, partialMarchDuke);
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void mustFetchLatestFillUpBefore() {
        final Vehicle dukeCar = this.getCreatedAndPersistedDukeCar();

        final Vehicle oakCar = new Vehicle().setOwner(dukeCar.getOwner()).setVehiclename("oak-car");
        this.em.persist(oakCar);

        final Date january = this.getMonth(0);
        final Date february = this.getMonth(1);
        final Date march = this.getMonth(2);
        final Date april = this.getMonth(3);

        final Refueling fillUpJanuary = this.createRefueling(dukeCar, january, true);
        this.em.persist(fillUpJanuary);

        final Refueling fillUpFebruary = this.createRefueling(dukeCar, february, true);
        this.em.persist(fillUpFebruary);

        final Refueling partialFillUpMarch = this.createRefueling(dukeCar, march, false);
        this.em.persist(partialFillUpMarch);

        final Refueling fillUpApril = this.createRefueling(dukeCar, april, true);
        this.em.persist(fillUpApril);

        final Optional<Refueling> actualResult = this.cut.getLatestFilledUpBefore(april);
        assertThat(actualResult).isPresent().contains(fillUpFebruary);
    }

    private Date getMonth(final int month) {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month);
        return calendar.getTime();
    }

    private Refueling createRefueling(final Vehicle vehicle, final Date date, final Boolean isFillUp) {
        return new Refueling.Builder().dateRefueled(date)
                .eurosPerLitre(1D)
                .fillUp(isFillUp)
                .kilometre(1D)
                .litres(1D)
                .vehicle(vehicle)
                .build();
    }
}
