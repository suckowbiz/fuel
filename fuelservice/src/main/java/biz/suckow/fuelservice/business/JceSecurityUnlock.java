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

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class JceSecurityUnlock {
    public static final String FIELD_NAME = "isRestricted";
    public static final String CLASS_NAME = "javax.crypto.JceSecurity";

    @Inject
    private Logger logger;

    @PostConstruct
    void unlockJceStrengthRestriction() {
        try {
            // remove restriction to be able to use jce for JOSE token
            java.lang.reflect.Field field = Class.forName(CLASS_NAME).getDeclaredField(FIELD_NAME);
            field.setAccessible(true);
            field.set(null, Boolean.FALSE);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Hacking JCE restriction failed: {0}", ex.getMessage());
        }
    }
}
