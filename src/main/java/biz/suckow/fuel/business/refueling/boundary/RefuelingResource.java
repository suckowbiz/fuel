package biz.suckow.fuel.business.refueling.boundary;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import biz.suckow.fuel.business.refueling.control.RefuelingService;
import biz.suckow.fuel.business.refueling.entity.RefuelingMeta;
import biz.suckow.fuel.business.vehicle.boundary.VehicleLocator;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@Path("refuelings")
@Stateless
public class RefuelingResource {
    @Inject
    private RefuelingService service;

    @Inject
    private VehicleLocator vehicleService;

    @POST
    @Path("/station/full/{username}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@PathParam("username") String username,
	    @PathParam("vehiclename") String vehiclename, RefuelingMeta meta) {
	Vehicle vehicle = this.vehicleService.getVehicle(username, vehiclename);
	this.service.fullTankRefuel(vehicle, meta.kilometers,
		meta.litresToTank, meta.eurosPerLitre, meta.date, meta.memo);
	return Response.ok().build();
    }

    @POST
    @Path("/station/partial/{username}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response partialRefuel(@PathParam("username") String username,
	    @PathParam("vehiclename") String vehiclename, RefuelingMeta meta) {
	Vehicle vehicle = this.vehicleService.getVehicle(username, vehiclename);
	this.service.partialTankRefuel(vehicle, meta.litresToTank,
		meta.eurosPerLitre, meta.date, meta.memo);
	return Response.ok().build();
    }

    @POST
    @Path("/station/all/{username}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response toTankAndStock(@PathParam("username") String username,
	    @PathParam("vehiclename") String vehiclename, RefuelingMeta meta) {
	Vehicle vehicle = this.vehicleService.getVehicle(username, vehiclename);
	this.service.fullTankAndStockRefuel(vehicle, meta.kilometers,
		meta.litresToTank, meta.litresToStock, meta.eurosPerLitre,
		meta.date, meta.memo);
	return Response.ok().build();
    }

    @POST
    @Path("/station/stock/{username}/{vehiclename}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuelStock(@PathParam("username") String username,
	    @PathParam("vehiclename") String vehiclename, RefuelingMeta meta) {
	Vehicle vehicle = this.vehicleService.getVehicle(username, vehiclename);
	this.service.stockRefuel(vehicle, meta.litresToStock,
		meta.eurosPerLitre, meta.date, meta.memo);
	return Response.ok().build();
    }

}
