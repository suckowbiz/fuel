package biz.suckow.fuelservice.business.vehicle.boundary;

/*
 * #%L
 * fuelservice
 * %%
 * Copyright (C) 2014 - 2015 Suckow.biz
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

import biz.suckow.fuelservice.business.owner.entity.CurrentIdentity;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequestScoped
public class OwnedVehicleValidator implements ConstraintValidator<OwnedVehicle, String> {
    @CurrentIdentity
    @Inject
    private OwnerPrincipal principal;

    @Inject
    private VehicleStore vehicleStore;

    @Override
    public void initialize(OwnedVehicle ownedVehicle) {
        /* NOP */
    }

    @Override
    public boolean isValid(String vehicleName, ConstraintValidatorContext constraintValidatorContext) {
        boolean result = false;
        for (String ownedVehicleName : this.principal.getOwnedVehicleNames()) {
            if (ownedVehicleName.equals(vehicleName)) {
                result = true;
                break;
            }
        }
        return result;
    }
}
