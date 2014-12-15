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

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class RESTBlackBoxIT extends RESTSupport {


    private String URI = "http://localhost:8080/fuelservice-st/resources/refuelings/index";

    @BeforeClass
    @Override
    public void init() {
	super.init(this.URI);
    }

    @Test
    public void activateMonitoring() {
	System.out.println("hello");

	//	Response response = super.mainTarget.request().get();
	//	assertThat(response.getStatus()).isEqualTo(204);
    }

}
