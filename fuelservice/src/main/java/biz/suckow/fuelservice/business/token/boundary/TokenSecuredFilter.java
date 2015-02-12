package biz.suckow.fuelservice.business.token.boundary;

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
import biz.suckow.fuelservice.business.owner.entity.Role;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@TokenSecured
@Provider
public class TokenSecuredFilter implements ContainerRequestFilter {
    @Context
    private ResourceInfo resourceInfo;

    @ClientIdentity
    @Inject
    private Instance<OwnerPrincipal> principalFactory;

    @Override
    public void filter(final ContainerRequestContext containerRequestContext) throws IOException {
        final TokenSecured annotation = this.resourceInfo.getResourceMethod().getAnnotation(TokenSecured.class);
        final List<Role> rolesAllowed = Arrays.asList(annotation.value());
        final Set<Role> actualRoles = this.principalFactory.get().getRoles();
        boolean notInRole = Collections.disjoint(actualRoles, rolesAllowed);
        if (notInRole) {
            containerRequestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        }
        boolean loggedOut = principalFactory.get().isLoggedOut();
        if (loggedOut) {
            containerRequestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                    .entity("Being logged out requests must not be submitted.")
                    .build());
        }
    }
}
