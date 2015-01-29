package biz.suckow.fuelservice.business.refuelling.control;

/*
 * #%L
 * fuelservice
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

import biz.suckow.fuelservice.business.consumption.entity.FillUpEvent;

import javax.enterprise.event.Event;
import javax.inject.Inject;
import java.util.Objects;

public class FillUpEventGun {
    private final Event<FillUpEvent> fillUpEvent;

    @Inject
    public FillUpEventGun(final Event<FillUpEvent> fillUpEvent) {
        this.fillUpEvent = fillUpEvent;
    }

    public void fire(final Long id) {
        Objects.requireNonNull(id, "Refuelling id must not be null.");
        final FillUpEvent event = new FillUpEvent().setRefuelingId(id);
        this.fillUpEvent.fire(event);
    }
}
