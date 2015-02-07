package biz.suckow.fuelservicest.business;

/*
 * #%L
 * fuelservice-st
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

import org.testng.annotations.Test;

import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "vehicle", dependsOnGroups = "login")
public class VehiclesResourceIT extends ArquillianBlackBoxTest {
    public static final String VEHICLE_NAME = "duke-car";

    @Test
    public void testAddVehicleSucceeds() {
        Response response = this.target.path("vehicles/{vehicle}")
                .resolveTemplate("vehicle", VEHICLE_NAME)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .post(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();
    }

    @Test
    public void testAddVehicleWithShortNameFails() {
        Response response = this.target.path("vehicles/{vehicle}")
                .resolveTemplate("vehicle", "s")
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .post(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        response.close();
    }

    @Test(dependsOnMethods = "testAddVehicleSucceeds")
    public void listVehiclesSucceeds() {
        final String crazyVehicleName = "My cräzy car°";
        Response response = this.target.path("vehicles/{vehicle}")
                .resolveTemplate("vehicle", crazyVehicleName)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .post(null);
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
        assertThat(values).hasSize(2).contains(VEHICLE_NAME, crazyVehicleName);
        response.close();
    }

    @Test(dependsOnMethods = "testAddVehicleSucceeds")
    public void testAddDuplicateVehicleFails() {
        Response response = this.target.path("vehicles/{vehicle}")
                .resolveTemplate("vehicle", VEHICLE_NAME)
                .request()
                .header("X-FUEL-TOKEN", AuthsResourceIT.token)
                .post(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        response.close();
    }
}
