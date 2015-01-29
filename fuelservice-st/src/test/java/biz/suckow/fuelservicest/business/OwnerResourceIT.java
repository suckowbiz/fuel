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

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class OwnerResourceIT extends ArquillianBlackBoxTest {
    private final String BASE = "http://localhost:8080/fuelservice-0.0.1-SNAPSHOT/resources/";
    private final Client client = ClientBuilder.newClient();

    @Test
    public void testRegisterOwnerSucceeds() {
        final Response response = this.client.target(BASE).path("owners/register/{email}/{password}")
                .resolveTemplate("email", "duke@java.net")
                .resolveTemplate("password", "password")
                .request()
                .post(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();
    }

    @Test(dependsOnMethods = "testRegisterOwnerSucceeds")
    public void testRequestTokenSucceeds() {
        Response response = this.client.target(BASE).path("owners/token/{email}/{password}")
                .resolveTemplate("email", "duke@java.net")
                .resolveTemplate("password", "password")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(String.class)).isNotNull().isNotEmpty();
        response.close();
    }

}
