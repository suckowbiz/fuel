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

import biz.suckow.fuelservice.business.refuelling.control.FillUpEventGun;
import biz.suckow.fuelservice.business.refuelling.control.FuelStockStore;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.vehicle.boundary.VehicleStore;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;
import java.util.Optional;

@Stateless
public class RefuellingStore {
    @Inject
    private EntityManager em;
    @Inject
    private FillUpEventGun gun;
    @Inject
    private FuelStockStore stockStore;
    @Inject
    private VehicleStore vehicleStore;

    public void add(final String vehicleName, final String ownerName, final RefuellingMeta meta) {
        // TODO handle previous additions /recalculate ...
        final Optional<Vehicle> possibleVehicle = this.vehicleStore.getVehicleByNameAndOwnerEmail(ownerName, vehicleName);
        final Vehicle vehicle = possibleVehicle.orElseThrow(() -> new IllegalStateException("No such vehicle."));
        if (meta.litresToStock > 0D) {
            this.stockStore.addition(vehicle, meta.date, meta.eurosPerLitre, meta.litresToStock);
        }
        if (meta.litresFromStock > 0D) {
            this.stockStore.release(possibleVehicle.get(), meta.date, meta.litresFromStock);
        }
        if (meta.isFull) {
            final Refuelling refuelling = this.storeFillUp(possibleVehicle.get(), meta.eurosPerLitre,
                    meta.litresToTank, meta.kilometre, meta.memo, meta.date);
            this.gun.fire(refuelling.getId());
        } else {
            this.storePartialRefueling(possibleVehicle.get(), meta.eurosPerLitre, meta.litresToTank,
                    meta.memo, meta.date);
        }
    }

    public Refuelling storeFillUp(final Vehicle vehicle, final Double eurosPerLitre, final Double litres, final Double kilometre, final String memo,
                                  final Date date) {
        final Refuelling result = new Refuelling.Builder().eurosPerLitre(eurosPerLitre).litres(litres)
                .kilometre(kilometre).memo(memo).dateRefueled(date).fillUp(true).vehicle(vehicle).build();
        this.em.persist(result);
        return result;
    }

    public void storePartialRefueling(final Vehicle vehicle, final Double euros, final Double litres, final String memo, final Date date) {
        final Refuelling refuelling = new Refuelling.Builder().litres(litres).eurosPerLitre(euros).dateRefueled(date)
                .memo(memo).vehicle(vehicle).build();
        this.em.persist(refuelling);
    }


//    public Set<Refuelling> list(String vehicleName) {
//        this.refuellingStore.g
//    }
}
