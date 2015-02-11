package biz.suckow.fuelservice.business.owner.boundary;

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

import biz.suckow.fuelservice.business.owner.entity.ClientIdentity;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.token.boundary.TokenService;
import biz.suckow.fuelservice.business.token.control.TokenValidationException;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@RequestScoped
public class OwnerPrincipalProducer {
    @Inject
    private HttpServletRequest request;

    @Inject
    private TokenService tokenService;

    @Inject
    private OwnerStore ownerStore;

    @ClientIdentity
    @Produces
    public OwnerPrincipal produce() throws TokenValidationException {
        final String token = request.getHeader(TokenService.TOKEN_HEADER_NAME);
        final String email = this.tokenService.readPrincipal(token);
        final OwnerPrincipal result = this.ownerStore.createOwnerPrincipal(email);
        return result;
    }

}
