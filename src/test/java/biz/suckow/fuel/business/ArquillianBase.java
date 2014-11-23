/*
 * Copyright 2014 Tobias Suckow.
 *
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
 */
package biz.suckow.fuel.business;

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.eu.ingwar.tools.arquillian.extension.suite.annotations.ArquillianSuiteDeployment;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OverProtocol;
import org.jboss.arquillian.testng.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PomEquippedResolveStage;
import org.jboss.shrinkwrap.resolver.api.maven.ScopeType;

import biz.suckow.fuel.business.owner.entity.Owner;
import biz.suckow.fuel.business.vehicle.entity.Vehicle;

@ArquillianSuiteDeployment
@Transactional(value = TransactionMode.ROLLBACK)
public abstract class ArquillianBase extends Arquillian {
    @Inject
    protected EntityManager em;

    @Deployment
    @OverProtocol("Servlet 3.0")
    public static WebArchive createDeployment() {
        final PomEquippedResolveStage resolver = Maven.resolver().loadPomFromFile("pom.xml");
        return ShrinkWrap.create(WebArchive.class)
                .addPackages(true, "biz.suckow.fuel")
                .addPackages(true, "org.h2")
                .addPackages(true, "com.google")
                .addAsLibraries(resolver.resolve("org.assertj:assertj-core").withTransitivity().asFile())
                .addAsLibraries(resolver.resolve("org.assertj:assertj-guava").withTransitivity().asFile())
                .addAsLibraries(
                        resolver.importDependencies(ScopeType.COMPILE, ScopeType.RUNTIME)
                                .resolve()
                                .withTransitivity()
                                .asFile())
                        .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                        .addAsResource("persistence.xml", "META-INF/persistence.xml");
    }

    protected Vehicle getCreatedAndPersistedDukeCar() {
        final Owner owner = new Owner();
        owner.setOwnername("duke");
        this.em.persist(owner);

        final Vehicle vehicle = new Vehicle();
        vehicle.setOwner(owner);
        vehicle.setVehiclename("duke-car");
        this.em.persist(vehicle);

        return vehicle;
    }
}
