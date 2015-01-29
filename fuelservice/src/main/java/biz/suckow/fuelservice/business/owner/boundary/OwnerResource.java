package biz.suckow.fuelservice.business.owner.boundary;

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

import biz.suckow.fuelservice.business.login.boundary.LoginService;
import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.security.control.TokenService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path(OwnerResource.PATH_BASE)
public class OwnerResource {
    public static final String PATH_BASE = "owners";

    @Inject
    private LoginService loginService;

    @Inject
    private TokenService tokenService;

    @Inject
    private OwnerService ownerService;

    @POST
    @Produces(APPLICATION_JSON)
    @Path("register/{email}/{password}")
    public Response register(@PathParam("email") String email, @PathParam("password") String password) {
        Response response;
        Optional<Owner> possibleOwner = this.ownerService.locateByEmail(email);
        if (possibleOwner.isPresent()) {
            response = Response.status(Response.Status.FORBIDDEN).entity("Please use another email address.").build();
        } else {
            this.ownerService.create(email, password);
            response = Response.ok().build();
        }
        return response;
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("token/{email}/{password}")
    public Response generateToken(@PathParam("email") String email, @PathParam("password") String password) {
        Response response = Response.status(Response.Status.UNAUTHORIZED).build();
        Optional<Owner> possibleOwner = this.loginService.login(email, password);
        if (possibleOwner.isPresent()) {
            String token = tokenService.generateToken(possibleOwner.get().getEmail());
            response = Response.ok(token).build();
        }
        return response;
    }
}
