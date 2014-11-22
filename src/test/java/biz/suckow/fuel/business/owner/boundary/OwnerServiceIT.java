package biz.suckow.fuel.business.owner.boundary;

import static org.assertj.guava.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.entity.Owner;

import com.google.common.base.Optional;

public class OwnerServiceIT extends ArquillianBase {
    @Inject
    private OwnerService cut;

    @Test
    public void ownernameMustBeUnique() {
        final Owner duke = new Owner().setOwnername("duke");
        this.em.persist(duke);
        final Optional<Owner> actualResult = this.cut.getOwner("duke");
        assertThat(actualResult).isPresent().contains(duke);
    }
}
