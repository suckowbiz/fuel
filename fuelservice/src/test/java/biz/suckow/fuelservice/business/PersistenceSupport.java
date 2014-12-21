package biz.suckow.fuelservice.business;

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
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.easymock.EasyMockSupport;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class PersistenceSupport extends EasyMockSupport {
    private static final String PERSISTENCE_UNIT_NAME = "integrationtest";
    protected static EntityManager em;
    protected static EntityTransaction tx;

    static {
	em = Persistence.createEntityManagerFactory(PersistenceSupport.PERSISTENCE_UNIT_NAME).createEntityManager();
	tx = em.getTransaction();
    }

    @BeforeMethod
    protected void startTransaction() {
	tx.begin();
    }

    @AfterMethod
    protected void rollbackTransaction() {
	tx.rollback();
    }

}
