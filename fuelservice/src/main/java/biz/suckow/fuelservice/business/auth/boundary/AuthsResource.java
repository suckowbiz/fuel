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

import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.token.boundary.TokenService;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("auths")
@Stateless
public class AuthsResource {
    @Inject
    private AuthService loginService;

    @Inject
    private TokenService tokenService;

    @Path("{email}")
    @DELETE
    @TokenSecured
    public Response logout(@PathParam("email") final String email) {
        this.loginService.logout(email);
        return Response.ok().build();
    }

    @Path("token/{email}/{password}")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response login(@PathParam("email") final String email, @PathParam("password") final String password) {
        Response response = Response.status(Response.Status.UNAUTHORIZED).build();
        final Optional<Owner> possibleOwner = this.loginService.login(email, password);
        if (possibleOwner.isPresent()) {
            final String token = tokenService.generateToken(possibleOwner.get().getEmail());
            response = Response.ok(token).build();
        }
        return response;
    }
}
