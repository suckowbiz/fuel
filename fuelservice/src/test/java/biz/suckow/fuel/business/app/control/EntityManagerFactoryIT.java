package biz.suckow.fuel.business.app.control;

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

import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.testng.annotations.Test;

import biz.suckow.fuel.business.ArquillianBase;

public class EntityManagerFactoryIT extends ArquillianBase {
    @Inject
    private EntityManager em;

    @Test
    public void injectionMustSucceed() {
	assertThat(this.em).isNotNull();
    }
}
