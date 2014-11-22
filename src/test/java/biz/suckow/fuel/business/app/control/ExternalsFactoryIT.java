package biz.suckow.fuel.business.app.control;

import javax.inject.Inject;

import org.testng.Assert;
import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;

public class ExternalsFactoryIT extends ArquillianBase {
    @Inject
    private LoggerFactory factory;

    @Test
    public void procudeLogger() {
        Assert.assertNotNull(this.factory);
    }
}
