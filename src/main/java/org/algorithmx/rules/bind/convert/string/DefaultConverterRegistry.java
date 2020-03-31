/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.bind.convert.string;

import org.algorithmx.rules.bind.convert.Converter;
import org.algorithmx.rules.spring.util.Assert;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Default implementation of the ConverterRegistry.
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public class DefaultConverterRegistry implements ConverterRegistry {

    private Map<Type, Map<Type, Converter>> converters = new HashMap<>();

    /**
     * Ctor to create a new ConverterRegistry.
     *
     * @param registerDefaults register all the standard converters.
     */
    public DefaultConverterRegistry(boolean registerDefaults) {
        super();
        if (registerDefaults) init();
    }

    /**
     * Register a new Converter. It will overwrite any existing converters that have the same source/target combo.
     *
     * @param converter new converter.
     */
    public void register(Converter<?, ?> converter) {
        Assert.notNull(converter, "Converter cannot be null.");

        Map<Type, Converter> map = converters.get(converter.getSourceType());

        if (map == null) {
            map = new HashMap<>();
            converters.put(converter.getSourceType(), map);
        }

        map.put(converter.getTargetType(), converter);
    }

    /**
     * Finds a Convert for the desired source/target types.
     *
     * @param source source type.
     * @param target target type.
     * @param <S> source generic type.
     * @param <T> target generic type.
     * @return converter if one is found; null otherwise.
     */
    public <S, T> Converter<S, T> find(Type source, Type target) {
        Map<Type, Converter> result = converters.get(source);
        return result.get(target);
    }

    private void init() {
        register(new StringToBigDecimalConverter());
        register(new StringToBigIntegerConverter());
        register(new StringToBooleanConverter());
        register(new StringToByteConverter());
        register(new StringToDoubleConverter());
        register(new StringToFloatConverter());
        register(new StringToIntegerConverter());
        register(new StringToLongConverter());
        register(new StringToShortConverter());
        register(new StringToStringConverter());
    }
}
