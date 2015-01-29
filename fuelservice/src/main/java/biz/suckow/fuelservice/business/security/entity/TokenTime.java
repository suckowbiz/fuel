package biz.suckow.fuelservice.business.security.entity;

/*
 * #%L
 * fuelservice
 * %%
 * Copyright (C) 2014 - 2015 Suckow.biz
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

public class TokenTime {
    private long issuedAt;
    private long expiresAt;

    public long getExpiresAt() {
        return expiresAt;
    }

    public TokenTime setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
        return this;
    }

    public long getIssuedAt() {
        return issuedAt;
    }

    public TokenTime setIssuedAt(long issuedAt) {
        this.issuedAt = issuedAt;
        return this;
    }
}
