package biz.suckow.fuelservice.business.token.boundary;

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


import biz.suckow.fuelservice.business.token.control.TokenTimeAuthority;
import biz.suckow.fuelservice.business.token.control.TokenValidationException;
import biz.suckow.fuelservice.business.token.entity.TokenSecret;
import biz.suckow.fuelservice.business.token.entity.TokenSignature;
import biz.suckow.fuelservice.business.token.entity.TokenTime;
import org.jboss.resteasy.jose.jwe.JWEBuilder;
import org.jboss.resteasy.jose.jwe.JWEInput;
import org.jboss.resteasy.jose.jws.JWSBuilder;
import org.jboss.resteasy.jose.jws.JWSInput;
import org.jboss.resteasy.jose.jws.crypto.RSAProvider;
import org.jboss.resteasy.jwt.JsonSerialization;
import org.jboss.resteasy.jwt.JsonWebToken;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

@Stateless
public class TokenService {
    public static final String TOKEN_HEADER_NAME = "X-FUEL-TOKEN";

    @Inject
    private TokenSignature signature;

    @Inject
    private TokenSecret secret;

    @Inject
    private TokenTimeAuthority timeAuthority;

    public String readPrincipal(String token) throws TokenValidationException {
        if (token == null || token.isEmpty()) {
            throw new TokenValidationException("Token must not be absent.");
        }

        JWSInput jws = new JWSInput(token, ResteasyProviderFactory.getInstance());
        this.verify(jws);

        String jwe = jws.readContent(String.class);
        JsonWebToken jwt = this.decrypt(jwe);
        return jwt.getPrincipal();
    }

    public String generateToken(String principal) {
        if (principal == null || principal.isEmpty()) {
            throw new IllegalArgumentException("Principal must not be null or empty.");
        }

        TokenTime tokenTime = this.timeAuthority.generate();
        JsonWebToken jwt = new JsonWebToken()
                .issuer(principal)
                .issuedAt(tokenTime.getIssuedAt())
                .expiration(tokenTime.getExpiresAt())
                .principal(principal);

        String jwe = new JWEBuilder().content(jwt, MediaType.APPLICATION_JSON_TYPE)
                                     .dir(this.secret.get());

        String jws = new JWSBuilder()
                .content(jwe, MediaType.TEXT_PLAIN_TYPE)
                .rsa512(this.signature.getPrivateKey());
        return jws;
    }

    private JsonWebToken decrypt(String jwe) throws TokenValidationException {
        byte[] content = new JWEInput(jwe).decrypt(this.secret.get())
                                          .getRawContent();
        JsonWebToken jwt;
        try {
            jwt = JsonSerialization.fromBytes(JsonWebToken.class, content);
            if (jwt.isExpired()) {
                throw new TokenValidationException("Token expired.");
            }
        }
        catch (IOException e) {
            throw new TokenValidationException("Failure deserialize token.");
        }
        return jwt;
    }

    private void verify(JWSInput jws) throws TokenValidationException {
        boolean result = RSAProvider.verify(jws, this.signature.getPublicKey());
        if (result) {
            return;
        }
        throw new TokenValidationException("Signature verification failed.");
    }
}
