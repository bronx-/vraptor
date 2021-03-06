/***
 * Copyright (c) 2009 Caelum - www.caelum.com.br/opensource
 * All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package br.com.caelum.vraptor.converter;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.caelum.vraptor.Converter;
import br.com.caelum.vraptor.core.Converters;
import br.com.caelum.vraptor.ioc.Container;

public class CachedConverters implements Converters {

    private final Converters delegate;
    private final Map<Class<?>, Class<?>> cache = new HashMap<Class<?>, Class<?>>();

    private static final Logger logger = LoggerFactory.getLogger(CachedConverters.class);

    public CachedConverters(Converters converters) {
        this.delegate = converters;
    }

    public Converter<?> to(Class<?> type, Container container) {
        if (cache.containsKey(type)) {
            Class<?> converterType = cache.get(type);
            return (Converter<?>) container.instanceFor(converterType);
        }
        logger.debug("Caching converter " + type.getName());
        Converter<?> converter = delegate.to(type, container);
        cache.put(type, converter.getClass());
        return converter;
    }

    public void register(Class<? extends Converter<?>> converterClass) {
        throw new UnsupportedOperationException(
                "cannot add vr3 converters in cached converters container (or should we delegate?");
    }

	public boolean existsFor(Class<?> type, Container container) {
		if (cache.containsKey(type)) {
			return true;
		}
		return delegate.existsFor(type, container);
	}

}
