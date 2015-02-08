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

import biz.suckow.fuelservice.business.consumption.control.FuelConsumptionCalculator;
import biz.suckow.fuelservice.business.owner.entity.Authenticated;
import biz.suckow.fuelservice.business.owner.entity.OwnerPrincipal;
import biz.suckow.fuelservice.business.refuelling.entity.Refuelling;
import biz.suckow.fuelservice.business.refuelling.entity.RefuellingMeta;
import biz.suckow.fuelservice.business.token.entity.TokenSecured;
import biz.suckow.fuelservice.business.vehicle.boundary.OwnedVehicle;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.logging.Logger;

@Stateless
@Path("refuellings")
public class RefuellingsResource {
    // TODO verify: because once a full refuelling is added and the consumption
    // is calculated the addition of previous
    // partial refuelings cannot be accepted... or must be re-calculated
    @Inject
    private RefuellingStore refuellingStore;

    @Inject
    private Logger logger;

    @Inject
    FuelConsumptionCalculator calc;

    @Authenticated
    @Inject
    private OwnerPrincipal principal;

    @TokenSecured
    @POST
    @Path("{vehicleName}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response refuel(@OwnedVehicle @PathParam("vehicleName") final String vehicleName, final RefuellingMeta meta) {
        this.refuellingStore.add(vehicleName, this.principal.getName(), meta);
        return Response.ok().build();
    }

    @TokenSecured
    @GET
    @Path("{vehicleName}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response list(@OwnedVehicle @PathParam("vehicleName") final String vehicleName) {
        JsonArrayBuilder result = Json.createArrayBuilder();
        Set<Refuelling> refuellings = this.refuellingStore.getForOnwerEmailAndVehicleName(this.principal.getName(), vehicleName);
//        for (Refuelling refuelling : refuellings) {
//            if (refuelling.getConsumption() != null)
//                throw new IllegalArgumentException("YUNU");
//            Optional<BigDecimal> consumption = this.calc.computeConsumptionFor(refuelling);
//            if (consumption.isPresent())
//            refuelling.setConsumption(consumption.get().doubleValue());
//        }

        refuellings.forEach(new Consumer<Refuelling>() {
            @Override
            public void accept(Refuelling refuelling) {
                String consumption = "";
                if (refuelling.getConsumption() != null) {
                    consumption = String.format("%.2f", refuelling.getConsumption());
                }
                JsonObject json = Json.createObjectBuilder().add("date", refuelling.getDateRefuelled().toString())
                        .add("eur", refuelling.getEurosPerLitre())
                        .add("fillUp", refuelling.getIsFillUp().toString())
                        .add("km", refuelling.getKilometre())
                        .add("ltr", refuelling.getLitres())
                        .add("memo", refuelling.getMemo())
                        .add("consumption", consumption).build();
                result.add(json);
            }
        });
        return Response.ok().entity(result.build()).build();
    }
}
