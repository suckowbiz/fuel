package biz.suckow.fuel.business.consumption.control;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;

public class FuelStockLocatorIT extends ArquillianBase {
    @Inject
    private FuelStockLocator cut;

    @Inject
    private EntityManager em;

    @Test
    public void f() {
    }
}
