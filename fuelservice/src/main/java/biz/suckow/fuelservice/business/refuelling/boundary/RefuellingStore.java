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

import biz.suckow.fuelservice.business.consumption.boundary.FillUpEventConsumer;
import biz.suckow.fuelservice.business.consumption.entity.FillUpEvent;
import biz.suckow.fuelservice.business.refuelling.control.FillUpEventGun;
import biz.suckow.fuelservice.business.refuelling.control.FuelStockStore;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.vehicle.boundary.VehicleStore;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Stateless
public class RefuellingStore {
    @Inject
    EntityManager em;
    @Inject
    FillUpEventGun gun;
    @Inject
    FuelStockStore stockStore;
    @Inject
    VehicleStore vehicleStore;
    @Inject
    private Event<FillUpEvent> fillUpEvent;

    public void add(final String vehicleName, final String ownerName, final RefuellingMeta meta) {
        // TODO handle previous additions /recalculate ...
        final Vehicle vehicle = this.vehicleStore.getVehicleByNameAndOwnerEmail(ownerName, vehicleName)
                .orElseThrow(() -> new IllegalStateException("No such vehicle."));
        if (meta.litresToStock > 0D) {
            this.stockStore.addition(vehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
        }
        if (meta.litresFromStock > 0D) {
            this.stockStore.release(vehicle, meta.date, meta.litresFromStock);
        }

        Refuelling refuelling;
        if (meta.isFull) {
            refuelling = this.storeFillUp(vehicle, meta.eurosPerLitre,
                    meta.litresToTank, meta.kilometre, meta.memo, meta.date);
            final FillUpEvent event = new FillUpEvent().setRefuelling(refuelling);
            this.fillUpEvent.fire(event);
        } else {
            refuelling = this.storePartialRefueling(vehicle, meta.eurosPerLitre, meta.litresToTank,
                    meta.memo, meta.date);
        }
        vehicle.getRefuellings().add(refuelling);
        this.em.merge(vehicle);
    }

    public Refuelling storeFillUp(final Vehicle vehicle, final Double eurosPerLitre, final Double litres, final Long kilometre, final String memo,
                                  final Date date) {
        final Refuelling result = new Refuelling.Builder().eurosPerLitre(eurosPerLitre).litres(litres)
                .kilometre(kilometre).memo(memo).dateRefueled(date).fillUp(true).vehicle(vehicle).build();
        this.em.persist(result);
        return result;
    }

    public Refuelling storePartialRefueling(final Vehicle vehicle, final Double euros, final Double litres, final String memo, final Date date) {
        final Refuelling result = new Refuelling.Builder().litres(litres).eurosPerLitre(euros).dateRefueled(date)
                .memo(memo).vehicle(vehicle).build();
        this.em.persist(result);
        return result;
    }

    public Optional<Refuelling> getFillUpBefore(final Date date) {
        final List<Refuelling> refuellings = this.em
                .createNamedQuery(Refuelling.FIND_BY_FILLED_UP_AND_DATE_BEFORE, Refuelling.class)
                .setParameter("right", date, TemporalType.TIMESTAMP).getResultList();
        Optional<Refuelling> result = Optional.empty();
        if (refuellings.size() > 0) {
            result = Optional.of(refuellings.get(0));
        }
        return result;
    }

    public List<Refuelling> getPartialRefuellingsBetween(final Date left, final Date right, final Vehicle vehicle) {
        final List<Refuelling> result = this.em
                .createNamedQuery(Refuelling.FIND_PARTIALS_BY_VEHICLE_AND_DATE_BETWEEN, Refuelling.class)
                .setParameter("left", left).setParameter("right", right).setParameter("vehicle", vehicle)
                .getResultList();
        return result;
    }

    public Set<Refuelling> getForOnwerEmailAndVehicleName(String ownerEmail, String vehicleName) {
        Vehicle vehicle = this.vehicleStore.getVehicleByNameAndOwnerEmail(ownerEmail, vehicleName).orElseThrow(() -> new IllegalStateException("No such vehicle/ owner association."));
        Set<Refuelling> result = vehicle.getRefuellings();
        return result;
    }
}
