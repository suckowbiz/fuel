package biz.suckow.fuelservicest.business;

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

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.archive.importer.MavenImporter;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

@ArquillianSuiteDeployment
public abstract class ArquillianBlackBoxTest extends Arquillian {
    private final String BASE = "http://localhost:8080/fuelservice/resources/";
    protected final WebTarget target = ClientBuilder.newClient().target(BASE);

    @Deployment(testable = false)
    @OverProtocol("Servlet 3.0")
    public static WebArchive deploy() {
        final WebArchive war = ShrinkWrap.create(MavenImporter.class)
                .loadPomFromFile("../fuelservice/pom.xml").importBit uildOutput().as(WebArchive.class);
        war.delete("META-INF/persistence.xml");
        war.addAsResource("arquillian/persistence.xml", "META-INF/persistence.xml");
        return war;
    }
}
