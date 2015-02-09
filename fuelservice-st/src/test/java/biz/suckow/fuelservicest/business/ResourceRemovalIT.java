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
import javax.json.JsonValue;
import javax.json.stream.JsonParser;
import javax.sql.rowset.spi.SyncResolver;
import javax.ws.rs.core.Response;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@Test(dependsOnGroups = "refuelling")
public class ResourceRemovalIT extends ArquillianBlackBoxTest {

    @Test(dependsOnMethods = "testRemoveVehicleSucceeds")
    public void testRemoveOwnerSucceeds() {
        Response response = this.target.path("owners/{email}")
                .resolveTemplate("email", OwnersResourceIT.OWNER_EMAIL)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();
    }

    @Test(dependsOnMethods = "testRemoveRefuellingsSucceeds")
    public void testRemoveVehicleSucceeds() {
        Response response = this.target.path("vehicles/{vehicle}")
                .resolveTemplate("vehicle", VehiclesResourceIT.VEHICLE_NAME)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .delete();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();

        response = this.target.path("vehicles")
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        Set<String> values = new HashSet<>();
        response.readEntity(JsonArray.class).forEach(new Consumer<JsonValue>() {
            @Override
            public void accept(JsonValue jsonValue) {
                values.add(jsonValue.toString().replaceAll("\"", ""));
            }
        });
        assertThat(values).doesNotContain(VehiclesResourceIT.VEHICLE_NAME);
        response.close();
    }

    @Test
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
