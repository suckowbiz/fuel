package biz.suckow.fuel.business.owner.entity;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.exception.ConstraintViolationException;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;

public class OwnerIT extends ArquillianBase {
    @Test
    public void doublicateOwnernameMustNotPerist() {
        final Owner duke = new Owner().setOwnername("duke");
        this.em.persist(duke);

        final Owner duke2 = new Owner().setOwnername("duke");

        try {
            this.em.persist(duke2);
        } catch (final Throwable t) {
            assertThat(t).hasCauseInstanceOf(ConstraintViolationException.class);
        }
    }
}
