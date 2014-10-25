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

@Path("refuelings")
@Stateless
public class RefuelingResource {
    @Inject
    private RefuelingService service;

    /**
     * Adds a refueling. Representation of use case: Fill tank completely at gas
     * station.
     * 
     * @param username
     * @param meta
     * @return
     */
    @POST
    @Path("/station/full/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@PathParam("username") String username,
	    RefuelingMeta meta) {
	this.service.fullTankRefuel(username, meta.kilometers,
		meta.litresToTank, meta.eurosPerLitre, meta.date, meta.memo);
	return Response.ok().build();
    }

    /**
     * Adds a refueling. Representation of use case: Fill tank partially at gas
     * station.
     * 
     * @param username
     * @param meta
     * @return
     */
    @POST
    @Path("/station/partial/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response partialRefuel(@PathParam("username") String username,
	    RefuelingMeta meta) {
	this.service.partialTankRefuel(username, meta.litresToTank,
		meta.eurosPerLitre, meta.date, meta.memo);
	return Response.ok().build();
    }

    /**
     * Adds a refueling. Representation of use case: from station fills tank and
     * stock.
     * 
     * @param username
     * @param meta
     */
    @POST
    @Path("/station/all/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response toTankAndStock(@PathParam("username") String username,
	    RefuelingMeta meta) {
	this.service.fullTankAndStockRefuel(username, meta.kilometers,
		meta.litresToTank, meta.litresToStock, meta.eurosPerLitre,
		meta.date, meta.memo);
	return Response.ok().build();
    }

    /**
     * Adds a refueling. Representation of use case: from station fills stock.
     * 
     * @param username
     * @param meta
     */
    @POST
    @Path("/station/stock/{username}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuelStock(@PathParam("username") String username,
	    RefuelingMeta meta) {
	this.service.stockRefuel(username, meta.litresToStock,
		meta.eurosPerLitre, meta.date, meta.memo);
	return Response.ok().build();
    }

}
