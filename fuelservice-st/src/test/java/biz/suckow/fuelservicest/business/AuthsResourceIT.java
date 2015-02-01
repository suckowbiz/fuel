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

import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "login", dependsOnGroups = "owner")
public class AuthsResourceIT extends ArquillianBlackBoxTest {

    @Test
    public void testRequestTokenSucceeds() {
        Response response = this.target.path("auths/token/{email}/{password}")
                .resolveTemplate("email", "duke@java.net")
                .resolveTemplate("password", "password")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        assertThat(response.readEntity(String.class)).isNotNull().isNotEmpty();
        response.close();
    }

    @Test
    public void testRequestTokenFails() {
        Response response = this.target.path("auths/token/{email}/{password}")
                .resolveTemplate("email", "duke@java.net")
                .resolveTemplate("password", "illegal")
                .request(MediaType.TEXT_PLAIN)
                .get();
        assertThat(response.getStatus()).isEqualTo(Response.Status.UNAUTHORIZED.getStatusCode());
        response.close();
    }
}
