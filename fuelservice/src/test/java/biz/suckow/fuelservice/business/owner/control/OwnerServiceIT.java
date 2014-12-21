package biz.suckow.fuelservice.business.owner.control;

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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.testng.annotations.Test;

import biz.suckow.fuelservice.business.PersistenceSupport;
import biz.suckow.fuelservice.business.TestHelper;
import biz.suckow.fuelservice.business.owner.controller.OwnerLocator;
import biz.suckow.fuelservice.business.owner.entity.Owner;

public class OwnerServiceIT extends PersistenceSupport {
    private final OwnerLocator cut = new OwnerLocator(em);

    @Test
    public void locateOwnerMustSucceed() {
	final Owner duke = TestHelper.createDuke();
	em.persist(duke);

	final Optional<Owner> actualResult = this.cut.getOwner("duke");
	assertThat(actualResult.isPresent());
	assertThat(actualResult.get()).isSameAs(duke);
    }

    @Test
    public void locateNonExistingOwnerMustFail() {
	final Optional<Owner> actualResult = this.cut.getOwner("duke");
	assertThat(actualResult.isPresent()).isFalse();
    }
}
