package biz.suckow.fuelservice.business.refuelling.boundary;

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

import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.vehicle.boundary.VehicleStore;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Stateless
public class RefuellingStore {
    @Inject
    EntityManager em;

    @Inject
    private VehicleStore vehicleStore;

    public Refuelling storeFillUp(final Vehicle vehicle, final Double eurosPerLitre, final Double litres, final Long kilometre, final String memo,
                                  final Date date) {
        final Refuelling result = new Refuelling.Builder().eurosPerLitre(eurosPerLitre)
                                                          .litres(litres)
                                                          .kilometre(kilometre)
                                                          .memo(memo)
                                                          .dateRefueled(date)
                                                          .fillUp(true)
                                                          .vehicle(vehicle)
                                                          .build();
        this.em.persist(result);
        return result;
    }

    public Refuelling storePartialRefueling(final Vehicle vehicle, final Double euros, final Double litres, final String memo, final Date date) {
        final Refuelling result = new Refuelling.Builder().litres(litres)
                                                          .eurosPerLitre(euros)
                                                          .dateRefueled(date)
                                                          .memo(memo)
                                                          .vehicle(vehicle)
                                                          .build();
        this.em.persist(result);
        return result;
    }

    public Optional<Refuelling> getFillUpBefore(final Date date) {
        final List<Refuelling> refuellings = this.em
                .createNamedQuery(Refuelling.FIND_BY_FILLED_UP_AND_DATE_BEFORE, Refuelling.class)
                .setParameter("right", date, TemporalType.TIMESTAMP)
                .getResultList();
        Optional<Refuelling> result = Optional.empty();
        if (refuellings.size() > 0) {
            result = Optional.of(refuellings.get(0));
        }
        return result;
    }

    public Optional<Refuelling> getFillUpAfter(final Date date) {
        final List<Refuelling> refuellings = this.em
                .createNamedQuery(Refuelling.FIND_BY_FILLED_UP_AND_DATE_AFTER, Refuelling.class)
                .setParameter("left", date, TemporalType.TIMESTAMP)
                .getResultList();
        Optional<Refuelling> result = Optional.empty();
        if (refuellings.size() > 0) {
            result = Optional.of(refuellings.get(0));
        }
        return result;
    }

    public List<Refuelling> getPartialRefuellingsBetween(final Date left, final Date right, final Vehicle vehicle) {
        final List<Refuelling> result = this.em
                .createNamedQuery(Refuelling.FIND_PARTIALS_BY_VEHICLE_AND_DATE_BETWEEN, Refuelling.class)
                .setParameter("left", left)
                .setParameter("right", right)
                .setParameter("vehicle", vehicle)
                .getResultList();
        return result;
    }

    public Optional<Refuelling> getById(Long id) {
        Refuelling result = null;
        try {
            result = this.em.find(Refuelling.class, id);
        }
        catch (EntityNotFoundException e) {
           /* NOP */
        }
        return Optional.ofNullable(result);
    }


    public void remove(Refuelling refuelling) {
        this.em.remove(refuelling);
    }
}
