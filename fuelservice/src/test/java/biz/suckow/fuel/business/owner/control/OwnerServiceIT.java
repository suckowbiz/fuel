package biz.suckow.fuel.business.owner.control;

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

import static org.assertj.guava.api.Assertions.assertThat;

import javax.inject.Inject;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;
import biz.suckow.fuel.business.owner.controller.OwnerLocator;
import biz.suckow.fuel.business.owner.entity.Owner;

import com.google.common.base.Optional;

public class OwnerServiceIT extends ArquillianBase {
    @Inject
    private OwnerLocator cut;

    @Test
    public void ownernameMustBeUnique() {
        final Owner duke = new Owner().setOwnername("duke");
        this.em.persist(duke);
        final Optional<Owner> actualResult = this.cut.getOwner("duke");
        assertThat(actualResult).isPresent().contains(duke);
    }
}
