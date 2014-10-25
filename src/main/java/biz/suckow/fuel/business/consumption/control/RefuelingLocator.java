package biz.suckow.fuel.business.consumption.control;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import biz.suckow.fuel.business.refueling.entity.Refueling;

public class RefuelingLocator {
    @PersistenceContext
    private EntityManager em;

    public List<Refueling> getRefuelingsMissingConsumptionOldestFirst() {
	@SuppressWarnings("unchecked")
	List<Refueling> result = this.em.createNamedQuery(
		Refueling.BY_MISSING_CONSUMPTION_OLDEST_FIRST).getResultList();
	return result;
    }
}
