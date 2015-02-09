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
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@Test(dependsOnGroups = "vehicle")
public class RefuellingsResourceIT extends ArquillianBlackBoxTest {

    @Test
    public void testAddTwoFullRefuellingsSucceeds() throws InterruptedException {
        JsonObject json = Json.createObjectBuilder()
                .add("date", Instant.ofEpochMilli(0).toString())
                .add("eurosPerLitre", 1.129D)
                .add("isFull", true)
                .add("kilometre", 1)
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

    @Test(dependsOnMethods = "testAddTwoFullRefuellingsSucceeds")
    public void testListRefuellingsSucceeds() {
        Response response = this.target.path("refuellings/{vehicleName}").resolveTemplate("vehicleName", VehiclesResourceIT.VEHICLE_NAME).request().header("X-FUEL-TOKEN", AuthsResourceIT.token).get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        List<String> values = new ArrayList<>();
        response.readEntity(JsonArray.class).forEach(new Consumer<JsonValue>() {
            @Override
            public void accept(JsonValue jsonValue) {
                values.add(jsonValue.toString().replaceAll("\"", ""));
            }
        });

        assertThat(values).hasSize(2);
        assertThat(values.get(0)).contains(",eur:1.129,fillUp:true,km:130000,ltr:50.0,memo:duke-car refuelling,consumption:0.00");
        assertThat(values.get(1)).contains(",eur:1.129,fillUp:true,km:1,ltr:50.0,memo:duke-car refuelling,consumption:");
        response.close();
    }

    @Test(dependsOnMethods = "testListRefuellingsSucceeds")
    public void testRemoveRefuellingsSucceeds() {
        Response response = this.target.path("refuellings/{vehicleName}")
                .resolveTemplate("vehicleName", VehiclesResourceIT.VEHICLE_NAME)
                .request().header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .get();
        Assertions.assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

        JsonArray jsonArray = response.readEntity(JsonArray.class);
        response.close();

        JsonParser parser = Json.createParserFactory(null).createParser(jsonArray);
        while (parser.hasNext()) {
            JsonParser.Event event = parser.next();
            switch (event) {
                case KEY_NAME:
                    if (parser.getString().equals("id")) {
                        parser.next();
                        long primaryKey = parser.getLong();
                        Response deleteResponse = this.target.path("refuellings/{refuellingId}")
                                .resolveTemplate("refuellingId", primaryKey)
                                .request().header("X-FUEL-TOKEN", AuthsResourceIT.token)
                                .delete();
                        assertThat(deleteResponse.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
                        deleteResponse.close();
                    }
                default:
                    break;
            }
        }

        response = this.target.path("refuellings/{vehicleName}").resolveTemplate("vehicleName", VehiclesResourceIT.VEHICLE_NAME).request().header("X-FUEL-TOKEN", AuthsResourceIT.token).get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(JsonArray.class)).isEmpty();
        response.close();
    }

}
