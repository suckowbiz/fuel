package biz.suckow.fuel.business.consumption.control;

/*
 * #%L
 * fuel
 * %%
 * Copyright (C) 2014 Suckow.biz
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.math.BigDecimal;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import biz.suckow.fuel.business.consumption.entity.FillUpEvent;
import biz.suckow.fuel.business.consumption.entity.FuelConsumption;
import biz.suckow.fuel.business.refueling.entity.Refueling;

import com.google.common.base.Optional;

@Stateless
public class FillUpEventConsumer {
    @Inject
    private FuelConsumptionCalculator maths;

    @Inject
    private EntityManager em;

    @Asynchronous
    public void consume(@Observes(during = TransactionPhase.AFTER_SUCCESS) final FillUpEvent event) {
	final Refueling refueling = this.em.find(Refueling.class, event.getRefuelingId());
	final Optional<BigDecimal> possibleResult = this.maths.computeConsumptionFor(refueling);
	if (possibleResult.isPresent()) {
	    final FuelConsumption consumption = new FuelConsumption();
	    consumption.setDateComputed(refueling.getDateRefueled());
	    consumption.setLitresPerKilometre(possibleResult.get().doubleValue());
	    consumption.setVehicle(refueling.getVehicle());
	    this.em.persist(consumption);
	}
    }
}
