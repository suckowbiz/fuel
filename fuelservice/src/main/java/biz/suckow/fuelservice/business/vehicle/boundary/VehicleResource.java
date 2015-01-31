package biz.suckow.fuelservice.business.vehicle.boundary;

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

import biz.suckow.fuelservice.business.owner.entity.Role;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;

import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("vehicles")
public class VehicleResource {
    @Inject
    private VehicleService vehicleService;

    @TokenSecured(Role.OWNER)
    @POST
    @Path("{email}/{vehicle}")
    public Response addVehicle(@PathParam("email") String email, @PathParam("vehicle") String vehicleName) {
        this.vehicleService.addVehicle(email, vehicleName);
        return Response.ok().build();
    }
}
