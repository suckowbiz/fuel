package biz.suckow.fuel.business.consumption.control;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import biz.suckow.fuel.business.refueling.entity.Refueling;

public class FuelConsumptionMaths {
    @PersistenceContext
    private EntityManager em;

    public Double calculate(Refueling refueling) {
	// get right date interval
	Date rightBorder = refueling.getDateRefueled();

	// get left date interval
	Date leftBorder = (Date) this.em
		.createNamedQuery(Refueling.QueryByExistingConsumptionForDateNewestFirst.NAME)
		.setParameter(
			Refueling.QueryByExistingConsumptionForDateNewestFirst.PARAM_NAME,
			refueling.getDateRefueled(), TemporalType.TIMESTAMP)
		.getResultList();

	// get all partial refuelings within interval
	// figure out stock at time of right date interval
	// sum up all refuelings and what is left of stock
	// divide litres by kilometers from actual refueling minus kilometer
	// from left date refueling

	// TODO 3. Implement
	throw new UnsupportedOperationException();
    }
}
