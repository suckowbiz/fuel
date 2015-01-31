package biz.suckow.fuelservice.business;

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

import biz.suckow.fuelservice.business.owner.entity.Owner;
import org.jboss.resteasy.jose.jwe.JWEBuilder;
import org.jboss.resteasy.jose.jwe.JWEInput;
import org.jboss.resteasy.jose.jws.JWSBuilder;
import org.jboss.resteasy.jose.jws.JWSInput;
import org.jboss.resteasy.jose.jws.crypto.RSAProvider;
import org.jboss.resteasy.jwt.JsonSerialization;
import org.jboss.resteasy.jwt.JsonWebToken;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.testng.annotations.Test;

import javax.ws.rs.core.MediaType;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by root on 1/26/15.
 */
public class JSONTest {

    @Test
    public void test() throws Exception {
        // produce rid of requiring jce files :)
        try {
            java.lang.reflect.Field field = Class.forName("javax.crypto.JceSecurity").getDeclaredField("isRestricted");
            field.setAccessible(true);
            field.set(null, java.lang.Boolean.FALSE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        KeyPair keyPair = KeyPairGenerator.getInstance("RSA").generateKeyPair();
        Owner duke = TestHelper.createDuke();
        JsonWebToken jwt = new JsonWebToken().issuer(duke.getEmail()).issuedAt(System.currentTimeMillis()-1000).expiration(System.currentTimeMillis()-1 ).principal(duke.getEmail());
        String jwtContent = JsonSerialization.toString(jwt, true);

        String jwe = new JWEBuilder().content(jwtContent, MediaType.TEXT_PLAIN_TYPE).dir("PU^sb_MvqZ{$mwW.9&lwp3;^2:9fK;2\\tD[1w/[j(dAY/Np4H(JkCoMLh:ru|i");
        String jws = new JWSBuilder()
                .contentType(MediaType.TEXT_PLAIN_TYPE)
                .content(jwe, MediaType.TEXT_PLAIN_TYPE)
                .rsa512(keyPair.getPrivate());

        // verify origin
        JWSInput jwsIn = new JWSInput(jws, ResteasyProviderFactory.getInstance());
        assertThat(RSAProvider.verify(jwsIn, keyPair.getPublic())).isTrue();

        // encrypt
        String jweIn = (String)jwsIn.readContent(String.class);
        byte[] jwtIn = new JWEInput(jweIn).decrypt("PU^sb_MvqZ{$mwW.9&lwp3;^2:9fK;2\\tD[1w/[j(dAY/Np4H(JkCoMLh:ru|i").getRawContent();

        // read content - check expiration
        JsonWebToken json = JsonSerialization.fromBytes(JsonWebToken.class, jwtIn);

        System.out.println("json: "+json.getPrincipal()+" "+json.isExpired());

        assertThat(json).isNotNull();
    }
}
