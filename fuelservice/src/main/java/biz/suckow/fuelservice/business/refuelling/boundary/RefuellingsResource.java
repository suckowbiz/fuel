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

import biz.suckow.fuelservice.business.owner.entity.CurrentIdentity;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;
import biz.suckow.fuelservice.business.vehicle.boundary.OwnedVehicle;
import biz.suckow.fuelservice.business.vehicle.boundary.VehicleStore;
import biz.suckow.fuelservice.business.vehicle.entity.Vehicle;

import javax.ejb.Stateless;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;
import java.util.logging.Logger;

@Stateless
@Path("refuellings")
public class RefuellingsResource {
    @Inject
    private RefuellingStore refuellingStore;

    @Inject
    private Logger logger;

    @CurrentIdentity
    @Inject
    private Instance<OwnerPrincipal> principal;

    @Inject
    private RefuellingService service;

    @Inject
    private VehicleStore vehicleStore;

    @TokenSecured
    @POST
    @Path("{vehicleName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response add(@OwnedVehicle @PathParam("vehicleName") final String vehicleName, final RefuellingMeta meta) {
        this.service.add(vehicleName, this.principal.get().getName(), meta);
        return Response.ok()
                .build();
    }

    @TokenSecured
    @DELETE
    @Path("{refuellingId}")
    public Response remove(@PathParam("refuellingId") long refuellingId) {
        Response response = Response.status(Response.Status.NOT_FOUND)
                .build();
        Optional<Refuelling> possibleRefuelling = this.refuellingStore.getById(refuellingId);
        if (possibleRefuelling.isPresent()) {
            this.refuellingStore.remove(possibleRefuelling.get());
            response = Response.ok()
                    .build();
        }
        return response;
    }

    @TokenSecured
    @GET
    @Path("{vehicleName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@OwnedVehicle @PathParam("vehicleName") final String vehicleName) {
        JsonArrayBuilder result = Json.createArrayBuilder();
        Optional<Vehicle> vehicle = this.vehicleStore.getVehicleByNameAndOwnerEmail(this.principal.getName(),
                vehicleName);
        vehicle.get()
                .getRefuellings()
                .forEach(refuelling -> {
                    String consumption = "";
                    if (refuelling.getConsumption() != null) {
                        consumption = String.format("%.2f", refuelling.getConsumption());
                    }
                    JsonObject json = Json.createObjectBuilder()
                            .add("date", refuelling.getDateRefuelled()
                                    .toString())
                            .add("id", refuelling.getId())
                            .add("eur", refuelling.getEurosPerLitre())
                            .add("fillUp", refuelling.getIsFillUp()
                                    .toString())
                            .add("km", refuelling.getKilometre())
                            .add("ltr", refuelling.getLitres())
                            .add("memo", refuelling.getMemo())
                            .add("consumption", consumption)
                            .build();
                    result.add(json);
                });
        return Response.ok()
                .entity(result.build())
                .build();
    }
}
