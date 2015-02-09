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

import biz.suckow.fuelservice.business.owner.entity.Owner;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Stateless
@Path(OwnersResource.PATH_BASE)
public class OwnersResource {
    public static final String PATH_BASE = "owners";

    @Inject
    private OwnerStore ownerStore;

    @TokenSecured
    @DELETE
    @Path("{email}")
    public Response remove(@PathParam("email") String email) {
        this.ownerStore.removeByEmail(email);
        return Response.ok().build();
    }

    @POST
    @Produces(APPLICATION_JSON)
    @Path("{email}/{password}")
    public Response register(@Size(min = 8, max = 64) @PathParam("email") String email, @Size(min = 6, max = 255) @PathParam("password") String password) {
        Response response;
        Optional<Owner> possibleOwner = this.ownerStore.getByEmail(email);
        if (possibleOwner.isPresent()) {
            response = Response.status(Response.Status.FORBIDDEN).entity("Please use another email address.").build();
        } else {
            this.ownerStore.create(email, password);
            response = Response.ok().build();
        }
        return response;
    }

}
