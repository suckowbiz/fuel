package biz.suckow.fuelservice.business.auth.boundary;

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


import biz.suckow.fuelservice.business.owner.boundary.OwnerService;
import biz.suckow.fuelservice.business.owner.entity.Owner;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.util.Optional;

@Stateless
public class AuthService {
    @Inject
    private OwnerService ownerService;

    public Optional<Owner> login(String email, String password) {
        Optional<Owner> result = Optional.empty();
        Optional<Owner> possibleOwner = this.ownerService.locateByEmail(email);
        if (possibleOwner.isPresent()) {
            if (possibleOwner.get().getPassword().equals(password)) {
                result = possibleOwner;
            }
        }
        return result;
    }
}