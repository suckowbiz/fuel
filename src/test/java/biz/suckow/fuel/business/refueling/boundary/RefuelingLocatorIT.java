package biz.suckow.fuel.business.refueling.boundary;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.assertj.core.api.Assertions;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.consumption.control.RefuelingLocator;
import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.refueling.entity.Refueling;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

public class RefuelingLocatorIT extends ArquillianBase {
    @Inject
    private RefuelingLocator cut;

    @Inject
    private EntityManager em;

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void testLocateNothing() {
        final Owner duke = new Owner().setOwnername("duke");
        this.em.persist(duke);

        final Vehicle dukeCar = new Vehicle().setOwner(duke).setVehiclename("duke-car");
        this.em.persist(dukeCar);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 2);
        final Date march = calendar.getTime();

        final Refueling refuelingMarchPartial = new Refueling.Builder().dateRefueled(march)
                .eurosPerLitre(1D)
                .fillUp(false)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refuelingMarchPartial);

        final List<Refueling> actualResult = this.cut.getFilledUpAndMissingConsumptionOldestFirst();
        Assertions.assertThat(actualResult).isEmpty();
    }

    @Test
    @Transactional(value = TransactionMode.ROLLBACK)
    public void testLocateExactly() {
        final Owner duke = new Owner().setOwnername("duke");
        this.em.persist(duke);

        final Vehicle dukeCar = new Vehicle().setOwner(duke).setVehiclename("duke-car");
        this.em.persist(dukeCar);

        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, 0);
        final Date january = calendar.getTime();

        calendar.set(Calendar.MONTH, 1);
        final Date february = calendar.getTime();

        calendar.set(Calendar.MONTH, 2);
        final Date march = calendar.getTime();

        Refueling refueling = new Refueling.Builder().dateRefueled(january)
                .eurosPerLitre(1D)
                .fillUp(true)
                .kilometre(1D)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refueling);

        refueling = new Refueling.Builder().dateRefueled(february)
                .eurosPerLitre(1D)
                .fillUp(true)
                .kilometre(1D)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refueling);

        refueling = new Refueling.Builder().dateRefueled(march)
                .eurosPerLitre(1D)
                .fillUp(true)
                .kilometre(1D)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refueling);

        final Refueling refuelingMarchPartial = new Refueling.Builder().dateRefueled(march)
                .eurosPerLitre(1D)
                .fillUp(false)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refuelingMarchPartial);

        final List<Refueling> actualResult = this.cut.getFilledUpAndMissingConsumptionOldestFirst();
        Assertions.assertThat(actualResult).hasSize(3);
        Assertions.assertThat(actualResult.get(0).getDateRefueled()).isEqualTo(march);
        Assertions.assertThat(actualResult.get(1).getDateRefueled()).isEqualTo(february);
        Assertions.assertThat(actualResult.get(2).getDateRefueled()).isEqualTo(january);
    }
}
