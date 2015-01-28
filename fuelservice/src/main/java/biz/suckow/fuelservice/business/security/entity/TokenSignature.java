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

import javax.annotation.PostConstruct;
import javax.ejb.*;
import javax.inject.Inject;
import java.security.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class TokenSignature {
    @Inject
    private Logger logger;

    private KeyPair keyPair;

    @PostConstruct
    void init () {
        try {
            this.keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
           this.logger.log(Level.SEVERE, "Failure to create RSA key pair: {0}", e.getMessage());
        }
    }

    @Lock(LockType.READ)
    public PublicKey getPublicKey() {
        return this.keyPair.getPublic();
    }

    @Lock(LockType.READ)
    public PrivateKey getPrivateKey() {
        return this.keyPair.getPrivate();
    }

}