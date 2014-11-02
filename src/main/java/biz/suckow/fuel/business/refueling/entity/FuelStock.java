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
package biz.suckow.fuel.business.refueling.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import biz.suckow.fuel.business.app.entity.BaseEntity;

import com.google.common.collect.Lists;

@Entity
public class FuelStock extends BaseEntity {
    private static final long serialVersionUID = 2386152541780890783L;

    @OneToMany
    private List<Refueling> refuelings;

    @OneToMany
    private List<StockRelease> stockReleases;

    public FuelStock() {
	this.stockReleases = Lists.newArrayList();
	this.refuelings = Lists.newArrayList();
    }

    public void addRefueling(final Refueling refueling) {
	this.refuelings.add(refueling);
    }

    public void addStockRelease(final StockRelease out) {
	this.stockReleases.add(out);
    }

    public List<Refueling> getRefuelings() {
	return this.refuelings;
    }

    public List<StockRelease> getStockReleases() {
	return this.stockReleases;
    }

}
