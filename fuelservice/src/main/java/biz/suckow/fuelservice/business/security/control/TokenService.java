package biz.suckow.fuelservice.business.security.control;

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


import biz.suckow.fuelservice.business.security.entity.TokenSecret;
import biz.suckow.fuelservice.business.security.entity.TokenSignature;
import org.jboss.resteasy.jose.jwe.JWEInput;
import org.jboss.resteasy.jose.jws.JWSInput;
import org.jboss.resteasy.jose.jws.crypto.RSAProvider;
import org.jboss.resteasy.jwt.JsonSerialization;
import org.jboss.resteasy.jwt.JsonWebToken;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TokenService {
    @Inject
    private TokenSignature signature;

    @Inject
    private TokenSecret secret;

    @Inject
    private Logger logger;

    public String getValidPrincipal(String token) throws TokenValidationException {
        if (token == null || token.isEmpty()) {
            throw new TokenValidationException("Token must not be absent.");
        }

        JWSInput jws = new JWSInput(token, ResteasyProviderFactory.getInstance());
        this.verify(jws);

        String jwe = (String) jws.readContent(String.class);
        JsonWebToken jwt = this.decrypt(jwe);
        return jwt.getPrincipal();
    }

    private JsonWebToken decrypt(String jwe) throws TokenValidationException {
        byte[] content = new JWEInput(jwe).decrypt(this.secret.get()).getRawContent();
        JsonWebToken jwt = null;
        try {
            jwt = JsonSerialization.fromBytes(JsonWebToken.class, content);
            if (jwt.isExpired()) {
                throw new TokenValidationException("Token expired.");
            }
        } catch (IOException e) {
            this.logger.log(Level.SEVERE, "Failure deserialize jwe: {0}", jwe);
            throw new TokenValidationException("Failure deserialize token.");
        }
        return jwt;
    }

    private void verify(JWSInput jws) throws TokenValidationException {
        boolean result = RSAProvider.verify(jws, this.signature.getPublicKey());
        if (result) {
            return;
        } else {
            throw new TokenValidationException("Signature verification failed.");
        }
    }
}
