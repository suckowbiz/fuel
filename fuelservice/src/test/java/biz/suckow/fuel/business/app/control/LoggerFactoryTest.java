package biz.suckow.fuel.business.app.control;

/*
 * #%L
 * fuelservice
 * %%
 * Copyright (C) 2014 Suckow.biz
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

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.lang.reflect.Member;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;

import org.testng.annotations.Test;

public class LoggerFactoryTest {
    @Test
    public void produceLoggerMustBeOfRightName() {
	final Member member = new Member() {

	    @Override
	    public boolean isSynthetic() {
		return false;
	    }

	    @Override
	    public String getName() {
		return null;
	    }

	    @Override
	    public int getModifiers() {
		return 0;
	    }

	    @Override
	    public Class<?> getDeclaringClass() {
		return this.getClass();
	    }
	};
	InjectionPoint ip = new InjectionPoint() {

	    @Override
	    public boolean isTransient() {
		return false;
	    }

	    @Override
	    public boolean isDelegate() {
		return false;
	    }

	    @Override
	    public Type getType() {
		return null;
	    }

	    @Override
	    public Set<Annotation> getQualifiers() {
		return null;
	    }

	    @Override
	    public Member getMember() {
		return member;
	    }

	    @Override
	    public Bean<?> getBean() {
		return null;
	    }

	    @Override
	    public Annotated getAnnotated() {
		return null;
	    }
	};

	Logger actualLogger = new LoggerFactory().produceLogger(ip);
	assertThat(actualLogger.getName()).contains(this.getClass().getName()); // logger appends $1
    }
}
