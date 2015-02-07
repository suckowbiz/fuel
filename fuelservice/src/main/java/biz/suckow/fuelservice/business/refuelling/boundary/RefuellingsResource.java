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

import biz.suckow.fuelservice.business.owner.boundary.Authenticated;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;
import biz.suckow.fuelservice.business.vehicle.boundary.OwnedVehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Resource must not be an EJB. This is to support RolesAllowed without enterprise token configuration required.
 */
@Stateless
@Path("refuellings")
public class RefuellingsResource {
    // TODO verify: because once a full refuelling is added and the consumption
    // is calculated the addition of previous
    // partial refuelings cannot be accepted!
    @Inject
    private RefuellingService refuellingService;

    @Inject
    private Logger logger;

    @Authenticated
    @Inject
    private OwnerPrincipal principal;

    @TokenSecured
    @POST
    @Path("{vehicleName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@OwnedVehicle @PathParam("vehicleName") final String vehicleName, final RefuellingMeta meta) {
        this.refuellingService.add(vehicleName, this.principal.getName(), meta);
        return Response.ok().build();
    }
}
