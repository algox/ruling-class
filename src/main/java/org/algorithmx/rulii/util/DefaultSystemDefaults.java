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
import org.algorithmx.rulii.script.NoOpScriptProcessor;
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
    private ScriptProcessor scriptProcessor = null;

    public DefaultSystemDefaults() {
        super();
    }

    @Override
    public BindingMatchingStrategy createBindingMatchingStrategy() {
        return matchingStrategy;
    }

    @Override
    public ParameterResolver createParameterResolver() {
        return parameterResolver;
    }

    @Override
    public MethodResolver createMethodResolver() {
        return methodResolver;
    }

    @Override
    public MessageResolver createMessageResolver(String...baseNames) {
        if (baseNames == null || baseNames.length == 0) return messageResolver;
        return MessageResolver.create(baseNames);
    }

    @Override
    public MessageFormatter createMessageFormatter() {
        return messageFormatter;
    }

    @Override
    public ObjectFactory createObjectFactory() {
        return objectFactory;
    }

    @Override
    public EventProcessor createEventProcessor() {
        return EventProcessor.create();
    }

    @Override
    public ConverterRegistry createConverterRegistry() {
        return converterRegistry;
    }

    @Override
    public ScriptProcessor createScriptProcessor() {
        if (scriptProcessor == null) {
            ScriptProcessor result = ScriptProcessor.create();
            this.scriptProcessor = result != null ? result : new NoOpScriptProcessor();
        }
        return scriptProcessor;
    }

    @Override
    public Clock createClock() {
        return Clock.systemDefaultZone();
    }

    @Override
    public Locale createDefaultLocale() {
        return Locale.getDefault();
    }
}
