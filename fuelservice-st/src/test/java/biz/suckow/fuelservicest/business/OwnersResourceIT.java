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

import junit.framework.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.logging.Level;
import java.util.logging.Logger;

import static org.assertj.core.api.Assertions.assertThat;

@Test(groups = "owner")
public class OwnersResourceIT extends ArquillianBlackBoxTest {

    @Test
    public void testRegisterOwnerSucceeds() {
        final Response response = this.target.path("owners/register/{email}/{password}")
                .resolveTemplate("email", "duke@java.net")
                .resolveTemplate("password", "password")
                .request()
                .post(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        response.close();
    }

    @Test(dataProvider = "illegalOwnerData")
    public void testRegisterOwnerFails(String email, String password) {
        final Response response = this.target.path("owners/register/{email}/{password}")
                .resolveTemplate("email", email)
                .resolveTemplate("password", password)
                .request()
                .post(null);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
        response.close();
    }

    @DataProvider
    private Object[][] illegalOwnerData() {
        String password = "passwordwithatleast8chars";
        String email = "emailaddress@thatmakes.sense";
        return new Object[][]{ {"s", password}, {email, "p"}, {"d", "s"}};
    }

}
