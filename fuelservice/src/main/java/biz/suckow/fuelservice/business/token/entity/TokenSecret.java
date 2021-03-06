package biz.suckow.fuelservice.business.token.entity;

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

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
public class TokenSecret {
    public static final int STRENGTH_BIT = 512;

    @Inject
    private TokenSignature signature;

    private String secret;

    @PostConstruct
    void init() {
        final StringBuilder builder = new StringBuilder();
        byte[] privateKey = signature.getPrivateKey().getEncoded();
        for (int i = 0; i < this.STRENGTH_BIT; i++) {
            builder.append(privateKey[i]);
        }
        this.secret = builder.toString();
    }

    @Lock(LockType.READ)
    public String get() {
        return this.secret;
    }
}
