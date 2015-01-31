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

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path(OwnerResource.PATH_BASE)
public class OwnerResource {
    public static final String PATH_BASE = "owners";

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

}
