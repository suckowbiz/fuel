package biz.suckow.fuel.business.app.control;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.testng.Assert;
import org.testng.annotations.Test;

public class ExternalsFactoryIT extends Arquillian {

    @Inject
    private ExternalsFactory factory;

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addClass(ExternalsFactory.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                .addAsResource("persistence.xml", "META-INF/persistence.xml");
    }

    @Test
    public void procudeLogger() {
        Assert.assertNotNull(this.factory);
    }
}
