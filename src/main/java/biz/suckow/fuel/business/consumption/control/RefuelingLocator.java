package biz.suckow.fuel.business.consumption.control;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.refueling.entity.Refueling;

import com.google.common.base.Optional;

public class RefuelingLocator {
    @PersistenceContext
    private EntityManager em;

    // TODO test
    public List<Refueling> getRefuelingsMissingConsumptionOldestFirst() {
	@SuppressWarnings("unchecked")
	List<Refueling> result = this.em.createNamedQuery(
		Refueling.BY_MISSING_CONSUMPTION_OLDEST_FIRST).getResultList();
	return result;
    }

    // TODO test
    public Optional<Refueling> get(Refueling refueling) {
	@SuppressWarnings("unchecked")
	List<Refueling> refuelings = this.em
		.createNamedQuery(
			Refueling.QueryByExistingConsumptionForDateNewestFirst.NAME)
		.setParameter(
			Refueling.QueryByExistingConsumptionForDateNewestFirst.PARAM_NAME,
			refueling.getDateRefueled(), TemporalType.TIMESTAMP)
		.getResultList();
	Optional<Refueling> result = Optional.absent();
	if (refuelings.size() > 0) {
	    result = Optional.of(refuelings.get(0));
	}
	return result;
    }
}
