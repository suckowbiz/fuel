package biz.suckow.fuelservicest.business;

/*
 * #%L
 * fuelservice-st
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

import org.assertj.core.api.Assertions;
import org.testng.annotations.Test;

import javax.json.Json;
import javax.json.JsonObject;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;

@Test(dependsOnGroups = "vehicle")
public class RefuellingsResourceIT extends ArquillianBlackBoxTest {

    @Test
    public void testAddTwoFullRefuelingsSucceeds() {
        JsonObject json = Json.createObjectBuilder()
                .add("date", Instant.now().toString())
                .add("eurosPerLitre", 1.129D)
                .add("isFull", true)
                .add("kilometre", 130000)
                .add("litresFromStock", 0)
                .add("litresToStock", 0)
                .add("litresToTank", 50)
                .add("memo", "duke-car refuelling").build();
        Response response = this.target
                .path("refuellings/{vehicleName}")
                .resolveTemplate("vehicleName", VehiclesResourceIT.VEHICLE_NAME)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON_TYPE));
        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();

        json = Json.createObjectBuilder()
                .add("date", Instant.ofEpochMilli(System.currentTimeMillis()).toString())
                .add("eurosPerLitre", 1.129D)
                .add("isFull", true)
                .add("kilometre", 130000)
                .add("litresFromStock", 0)
                .add("litresToStock", 0)
                .add("litresToTank", 50)
                .add("memo", "duke-car refuelling").build();
        response = this.target
                .path("refuellings/{vehicleName}")
                .resolveTemplate("vehicleName", VehiclesResourceIT.VEHICLE_NAME)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .post(Entity.entity(json.toString(), MediaType.APPLICATION_JSON_TYPE));
        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();

    }

}
