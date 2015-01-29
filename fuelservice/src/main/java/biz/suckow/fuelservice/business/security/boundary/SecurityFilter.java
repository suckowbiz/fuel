package biz.suckow.fuelservice.business.security.boundary;

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
import biz.suckow.fuelservice.business.security.control.TokenService;
import biz.suckow.fuelservice.business.security.control.TokenValidationException;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class SecurityFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private TokenService tokenService;

    @Inject
    private OwnerService ownerService;

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Annotation[] declaredAnnotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            String abortReason = "";
            if (annotation instanceof RolesAllowed) {
                String[] roles = ((RolesAllowed) annotation).value();
                String token = containerRequestContext.getHeaderString("X-FUEL-TOKEN");
                this.logger.log(Level.INFO, "token: {0}", token);
                try {
                    boolean isAllowed = isAllowed(roles, token);
                    if (isAllowed == false) {
                        abortReason = "Forbidden.";
                    }
                } catch (TokenValidationException e) {
                    abortReason = e.getMessage();
                }
            } else if (annotation instanceof PermitAll) {
                /* NOP */
            } else if (annotation instanceof DenyAll) {
                abortReason = "Denied.";
            } else {
                continue;
            }

            if (abortReason.length() > 0) {
                this.logger.log(Level.INFO, "Request aborted: {0}", abortReason);
                containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
            }
            break;
        }
    }

    private boolean isAllowed(String[] roles, String token) throws TokenValidationException {
        boolean result = false;
        String principal = this.tokenService.readPrincipal(token);
        Optional<Owner> possibleOwner = this.ownerService.locateByEmail(principal);
        if (possibleOwner.isPresent()) {
            Owner owner = possibleOwner.get();
            for (String role : roles) {
                if (role.equalsIgnoreCase(owner.getRole().name())) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
}
