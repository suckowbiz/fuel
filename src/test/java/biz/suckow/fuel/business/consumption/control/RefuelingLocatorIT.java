package biz.suckow.fuel.business.consumption.control;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
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

        final Refueling refuelingMarchPartial = new Refueling.Builder()
                .dateRefueled(march)
                .eurosPerLitre(1D)
                .fillUp(false)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refuelingMarchPartial);

        final List<Refueling> actualResult = this.cut.getFilledUpAndMissingConsumptionOldestFirst();
        assertThat(actualResult.size(), is(0));
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

        Refueling refueling = new Refueling.Builder()
                .dateRefueled(january)
                .eurosPerLitre(1D)
                .fillUp(true)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refueling);

        refueling = new Refueling.Builder()
                .dateRefueled(february)
                .eurosPerLitre(1D)
                .fillUp(true)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refueling);

        refueling = new Refueling.Builder()
                .dateRefueled(march)
                .eurosPerLitre(1D)
                .fillUp(true)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refueling);

        final Refueling refuelingMarchPartial = new Refueling.Builder()
                .dateRefueled(march)
                .eurosPerLitre(1D)
                .fillUp(false)
                .litres(1D)
                .vehicle(dukeCar)
                .build();
        this.em.persist(refuelingMarchPartial);

        final List<Refueling> actualResult = this.cut.getFilledUpAndMissingConsumptionOldestFirst();
        assertThat(actualResult.size(), is(3));
        assertThat(actualResult.get(0).getDateRefueled(), is(march));
        assertThat(actualResult.get(1).getDateRefueled(), is(february));
        assertThat(actualResult.get(2).getDateRefueled(), is(january));
    }
}
