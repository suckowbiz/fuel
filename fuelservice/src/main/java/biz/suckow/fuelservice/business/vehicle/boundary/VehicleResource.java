package biz.suckow.fuelservice.business.vehicle.boundary;

import biz.suckow.fuelservice.business.owner.entity.RoleType;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("vehicles")
public class VehicleResource {
    @Inject
    private VehicleService vehicleService;

    @RolesAllowed({RoleType.OWNER})
    @POST
    @Path("{email}/{vehicle}")
    public Response addVehicle(@PathParam("email") String email, @PathParam("vehicle") String vehicleName) {
        this.vehicleService.addVehicle(email, vehicleName);
        return Response.ok().build();
    }
}
