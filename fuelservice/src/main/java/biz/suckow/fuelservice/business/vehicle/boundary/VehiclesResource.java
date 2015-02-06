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

import biz.suckow.fuelservice.business.owner.boundary.Authenticated;
import biz.suckow.fuelservice.business.owner.boundary.OwnerService;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.validation.constraints.Size;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@RequestScoped
@Path("vehicles")
public class VehiclesResource {
    @Authenticated
    @Inject
    private OwnerPrincipal principal;

    @Inject
    private VehicleService vehicleService;

    @Inject
    private OwnerService ownerService;

    @TokenSecured
    @POST
    @Path("{vehicle}")
    public Response addVehicle(@Size(min = 3, max = 64) @PathParam("vehicle") String vehicleName) {
        Set<String> ownedVehicles = this.principal.getOwnedVehicleNames();
        if (ownedVehicles.contains(vehicleName)) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Failure to add duplicate vehicle.").build();
        }

        String ownerEmail = this.principal.getName();
        this.vehicleService.addVehicle(ownerEmail, vehicleName);

        return Response.ok().build();
    }

    @TokenSecured
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response listAll() {
        Set<Vehicle> vehicles = this.vehicleService.getOwned(this.principal.getName());
        JsonArrayBuilder builder = Json.createArrayBuilder();
        for (Vehicle vehicle : vehicles) {
            builder.add(vehicle.getVehicleName());
        }
        return Response.ok().entity(builder.build()).build();
    }
}
