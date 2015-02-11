package biz.suckow.fuelservice.business.token.control;

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

import biz.suckow.fuelservice.business.token.entity.TokenTime;

import java.util.concurrent.TimeUnit;

public class TokenTimeAuthority {
    private static final long EXPIRATION_SECONDS = TimeUnit.MINUTES.toSeconds(15);

    public TokenTime generate() {
        long issuedAtSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long expiresAtSeconds = issuedAtSeconds + EXPIRATION_SECONDS;
        TokenTime result = new TokenTime().setExpiresAt(expiresAtSeconds)
                .setIssuedAt(issuedAtSeconds);
        return result;
    }
}
