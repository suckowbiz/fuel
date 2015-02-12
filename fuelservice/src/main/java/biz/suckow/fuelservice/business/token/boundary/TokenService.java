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


import biz.suckow.fuelservice.business.token.control.TokenValidationException;
import biz.suckow.fuelservice.business.token.entity.TokenSecret;
import biz.suckow.fuelservice.business.token.entity.TokenSignature;
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
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.concurrent.TimeUnit;

@Stateless
public class TokenService {
    private static final long TOKEN_EXPIRATION_SECONDS = TimeUnit.MINUTES.toSeconds(15);

    public static final String TOKEN_HEADER_KEY = "X-FUEL-TOKEN";

    @Inject
    private TokenSignature signature;

    @Inject
    private TokenSecret secret;

    public String readPrincipal(final String token) throws TokenValidationException {
        if (token == null || token.isEmpty()) {
            throw new TokenValidationException("Token must not be absent.");
        }
        final JWSInput jws = new JWSInput(token, ResteasyProviderFactory.getInstance());
        this.verify(jws);

        final String jwe = jws.readContent(String.class);
        final JsonWebToken jwt = this.decrypt(jwe);

        return jwt.getPrincipal();
    }

    public String generateToken(final String principal) {
        if (principal == null || principal.isEmpty()) {
            throw new IllegalArgumentException("Principal must not be null or empty.");
        }

        long issuedAtSeconds = TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
        long expiresAtSeconds = issuedAtSeconds + TOKEN_EXPIRATION_SECONDS;
        final JsonWebToken jwt = new JsonWebToken().issuer(principal).issuedAt(issuedAtSeconds)
                .expiration(expiresAtSeconds)
                .principal(principal);

        final String secret = this.secret.get();
        final String jwe = new JWEBuilder().content(jwt, MediaType.APPLICATION_JSON_TYPE).dir(secret);

        final PrivateKey privateKey = this.signature.getPrivateKey();
        final String jws = new JWSBuilder().content(jwe, MediaType.TEXT_PLAIN_TYPE).rsa512(privateKey);

        return jws;
    }

    private JsonWebToken decrypt(final String jwe) throws TokenValidationException {
        JsonWebToken jwt;
        try {
            final String secret = this.secret.get();
            final byte[] content = new JWEInput(jwe).decrypt(secret).getRawContent();
            jwt = JsonSerialization.fromBytes(JsonWebToken.class, content);
            if (jwt.isExpired()) {
                throw new TokenValidationException("Token expired.");
            }
        }
        catch (IOException e) {
            throw new TokenValidationException("Failure to deserialize token.");
        }
        return jwt;
    }

    private void verify(final JWSInput jws) throws TokenValidationException {
        final PublicKey publicKey = this.signature.getPublicKey();
        final boolean result = RSAProvider.verify(jws, publicKey);
        if (result) {
            return;
        }
        throw new TokenValidationException("Signature verification failed.");
    }
}
