/*
 * Copyright 2014 Tobias Suckow.
 *
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
 */
package biz.suckow.fuel.business.refueling.boundary;

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

import biz.suckow.fuel.business.refueling.control.RefuelingService;
import biz.suckow.fuel.business.refueling.entity.RefuelingMeta;
import biz.suckow.fuel.business.vehicle.boundary.VehicleLocator;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

// TODO test
@Path("refuelings")
@Stateless
public class RefuelingResource {
    // TODO verify: because once a full refueling is added and the consumption is calculated the addition of previous
    // partial refuelings cannot be accepted!
    @Inject
    private RefuelingService refuelingService;

    @Inject
    private VehicleLocator vehicleService;

    @GET
    @Path("index")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Refuelings Resource";
    }

    @POST
    @Path("station/full/{ownername}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@PathParam("ownername") final String ownername,
            @PathParam("vehiclename") final String vehiclename, final RefuelingMeta meta) {
        final Vehicle vehicle = this.vehicleService.getVehicle(ownername, vehiclename).get();
        this.refuelingService.fullTankRefuel(vehicle, meta.kilometre, meta.litresToTank, meta.eurosPerLitre, meta.date,
                meta.memo);
        return Response.ok().build();
    }

    @POST
    @Path("station/partial/{ownername}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response partialRefuel(@PathParam("ownername") final String ownername,
            @PathParam("vehiclename") final String vehiclename, final RefuelingMeta meta) {
        final Vehicle vehicle = this.vehicleService.getVehicle(ownername, vehiclename).get();
        this.refuelingService.partialTankRefuel(vehicle, meta.litresToTank, meta.eurosPerLitre, meta.date, meta.memo);
        return Response.ok().build();
    }

    @POST
    @Path("station/all/{ownername}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response toTankAndStock(@PathParam("ownername") final String ownername,
            @PathParam("vehiclename") final String vehiclename, final RefuelingMeta meta) {
        final Vehicle vehicle = this.vehicleService.getVehicle(ownername, vehiclename).get();
        this.refuelingService.fullTankAndStockRefuel(vehicle, meta.kilometre, meta.litresToTank, meta.litresToStock,
                meta.eurosPerLitre, meta.date, meta.memo);
        return Response.ok().build();
    }

    @POST
    @Path("station/stock/{ownername}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuelStock(@PathParam("ownername") final String ownername,
            @PathParam("vehiclename") final String vehiclename, final RefuelingMeta meta) {
        final Vehicle vehicle = this.vehicleService.getVehicle(ownername, vehiclename).get();
        this.refuelingService.stockAddition(vehicle, meta.litresToStock, meta.eurosPerLitre, meta.date, meta.memo);
        return Response.ok().build();
    }

}
