package biz.suckow.fuelservice.business.consumption.boundary;

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

import biz.suckow.fuelservice.business.consumption.control.FuelConsumptionCalculator;
import biz.suckow.fuelservice.business.consumption.entity.FillUpEvent;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;

import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.enterprise.event.Observes;
import javax.enterprise.event.TransactionPhase;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Optional;

@Stateless
public class FillUpEventConsumer {
    @Inject
    private FuelConsumptionCalculator maths;

    @Inject
    private EntityManager em;

    @Asynchronous
    public void consume(@Observes(during = TransactionPhase.AFTER_SUCCESS) final FillUpEvent event) {
        final Refuelling refuelling = this.em.find(Refuelling.class, event.getRefuelingId());
        final Optional<BigDecimal> possibleResult = this.maths.computeConsumptionFor(refuelling);
        if (possibleResult.isPresent()) {
            refuelling.setConsumption(possibleResult.get().doubleValue());
            this.em.merge(refuelling);
        }
    }
}
