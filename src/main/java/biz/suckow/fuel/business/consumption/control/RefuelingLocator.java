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
    public Optional<Refueling> get(Refueling refueling) {
	@SuppressWarnings("unchecked")
	List<Refueling> refuelings = this.em
		.createNamedQuery(
			Refueling.ByExistingConsumptionForDateNewestFirst.NAME)
		.setParameter(
			Refueling.ByExistingConsumptionForDateNewestFirst.PARAM_NAME,
			refueling.getDate(), TemporalType.TIMESTAMP)
		.getResultList();
	Optional<Refueling> result = Optional.absent();
	if (refuelings.size() > 0) {
	    result = Optional.of(refuelings.get(0));
	}
	return result;
    }
}
