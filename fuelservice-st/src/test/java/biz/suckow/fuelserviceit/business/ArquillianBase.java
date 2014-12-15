package biz.suckow.fuelserviceit.business;

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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;

@ArquillianSuiteDeployment
@Transactional(value = TransactionMode.ROLLBACK)
public abstract class ArquillianBase extends Arquillian {
    private static final String UNIT_TEST_PATTERN = ".*Test.*";

    @PersistenceContext
    protected EntityManager em;

    @Deployment
    @OverProtocol("Servlet 3.0")
    public static WebArchive deploy() {
	final PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
	return ShrinkWrap
		.create(WebArchive.class)
		.addPackages(true, Filters.exclude(ArquillianBase.UNIT_TEST_PATTERN),
			"biz.suckow.fuelserviceit.business")
			// .addAsLibraries(resolver.resolve("biz.suckow.fuel:fuelservice").withTransitivity().asFile())
			.addAsLibraries(resolver.resolve("org.easymock:easymock").withoutTransitivity().asFile())
			.addAsLibraries(resolver.resolve("com.h2database:h2").withoutTransitivity().asFile())
			.addAsLibraries(resolver.resolve("org.assertj:assertj-core").withoutTransitivity().asFile())
			.addAsLibraries(resolver.resolve("org.assertj:assertj-guava").withoutTransitivity().asFile())
			.addAsLibraries(resolver.importRuntimeDependencies().resolve().withoutTransitivity().asFile())
			.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
			.addAsResource("arquillian/persistence.xml", "META-INF/persistence.xml");
    }
}
