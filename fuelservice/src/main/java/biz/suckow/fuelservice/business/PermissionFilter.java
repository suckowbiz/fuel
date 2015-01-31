package biz.suckow.fuelservice.business;

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
import biz.suckow.fuelservice.business.token.control.TokenValidationException;
import biz.suckow.fuelservice.business.token.entity.Authenticated;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.security.Principal;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class PermissionFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private OwnerService ownerService;

    @Authenticated
    @Inject
    private Instance<Principal> principalProducer;

    @Inject
    private Logger logger;

    @Override
    public void filter(ContainerRequestContext containerRequestContext) throws IOException {
        Annotation[] annotations = resourceInfo.getResourceMethod().getDeclaredAnnotations();
        for (Annotation annotation : annotations) {
            String abortReason = "";
            if (annotation instanceof RolesAllowed) {
                String[] roles = ((RolesAllowed) annotation).value();
                try {
                    boolean isAllowed = isAllowed(roles);
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

    // https://github.com/lefloh/jax-rs-context/blob/master/src/main/java/de/utkast/rest/context/provider/UserProvider.java
    private boolean isAllowed(String[] roles) throws TokenValidationException {
        boolean result = false;
        String email = this.principalProducer.get().getName();
        Optional<Owner> possibleOwner = this.ownerService.locateByEmail(email);
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
