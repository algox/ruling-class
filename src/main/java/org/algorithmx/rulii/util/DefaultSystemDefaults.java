/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.util;

import org.algorithmx.rulii.bind.convert.ConverterRegistry;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.bind.match.ParameterResolver;
import org.algorithmx.rulii.event.EventProcessor;
import org.algorithmx.rulii.script.ScriptProcessor;
import org.algorithmx.rulii.text.MessageFormatter;
import org.algorithmx.rulii.text.MessageResolver;
import org.algorithmx.rulii.util.reflect.MethodResolver;
import org.algorithmx.rulii.util.reflect.ObjectFactory;

import java.time.Clock;
import java.util.Locale;

public class DefaultSystemDefaults implements SystemDefaults {

    private final BindingMatchingStrategy matchingStrategy = BindingMatchingStrategy.create();
    private final ParameterResolver parameterResolver = ParameterResolver.create();
    private final MethodResolver methodResolver = MethodResolver.create();
    private final MessageResolver messageResolver = MessageResolver.create();
    private final MessageFormatter messageFormatter = MessageFormatter.create();
    private final ObjectFactory objectFactory = ObjectFactory.create();
    private final ConverterRegistry converterRegistry = ConverterRegistry.create();
    private String scriptingLanguage = ScriptProcessor.JAVASCRIPT;

    public DefaultSystemDefaults() {
        super();
    }

    @Override
    public BindingMatchingStrategy getBindingMatchingStrategy() {
        return matchingStrategy;
    }

    @Override
    public ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    @Override
    public MethodResolver getMethodResolver() {
        return methodResolver;
    }

    @Override
    public MessageResolver getMessageResolver() { return messageResolver; }

    @Override
    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    @Override
    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    @Override
    public ConverterRegistry getConverterRegistry() {
        return converterRegistry;
    }

    @Override
    public String getScriptingLanguage() { return scriptingLanguage; }

    @Override
    public Clock getClock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public Locale getDefaultLocale() {
        return Locale.getDefault();
    }
}
