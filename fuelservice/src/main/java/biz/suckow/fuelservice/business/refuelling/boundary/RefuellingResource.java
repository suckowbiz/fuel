package biz.suckow.fuelservice.business.refuelling.boundary;

/*
 * #%L
 * fuel
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

import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

// TODO test

/**
 * Resource must not be an EJB. This is to support RolesAllowed without enterprise security configuration required.
 */
@Path("refuellings")
public class RefuellingResource {
    // TODO verify: because once a full refuelling is added and the consumption
    // is calculated the addition of previous
    // partial refuelings cannot be accepted!
    @Inject
    private RefuellingService refuellingService;

    @RolesAllowed("OWNER")
    // @PermitAll
    @GET
    public Response index() {
        return Response.ok().entity(this.getClass().getSimpleName()).build();
    }

    @RolesAllowed("ARole")
    @POST
    @Path("add/{ownerName}/{vehicleName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@PathParam("ownerName") final String ownerName,
                           @PathParam("vehicleName") final String vehicleName, final RefuellingMeta meta) {
        this.refuellingService.add(vehicleName, ownerName, meta);
        return Response.ok().build();
    }
}
