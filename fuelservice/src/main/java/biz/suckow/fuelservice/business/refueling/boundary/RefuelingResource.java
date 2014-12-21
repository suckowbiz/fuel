package biz.suckow.fuelservice.business.refueling.boundary;

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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import biz.suckow.fuelservice.business.refueling.control.RefuelingService;
import biz.suckow.fuelservice.business.refueling.entity.RefuelingMeta;

// TODO test
@Path("refuelings")
@Stateless
public class RefuelingResource {
    // TODO verify: because once a full refueling is added and the consumption
    // is calculated the addition of previous
    // partial refuelings cannot be accepted!
    @Inject
    private RefuelingService refuelingService;

    @GET
    @Path("index")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
	return "Refuelings Resource";
    }

    @POST
    @Path("add/{ownerName}/{vehicleName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@PathParam("ownerName") final String ownerName,
	    @PathParam("vehicleName") final String vehicleName, final RefuelingMeta meta) {
	this.refuelingService.add(vehicleName, ownerName, meta);
	return Response.ok().build();
    }
}