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

package org.algorithmx.rulii.config;

import org.algorithmx.rulii.bind.convert.ConverterRegistry;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.bind.match.ParameterResolver;
import org.algorithmx.rulii.script.ScriptProcessor;
import org.algorithmx.rulii.text.MessageFormatter;
import org.algorithmx.rulii.text.MessageResolver;
import org.algorithmx.rulii.util.reflect.MethodResolver;
import org.algorithmx.rulii.util.reflect.ObjectFactory;

import java.time.Clock;
import java.util.Locale;

public final class RuliiSystem {

    private static final RuliiSystem instance = new RuliiSystem();

    private RuliiConfiguration configuration;

    private RuliiSystem() {
        super();
        init();
    }

    public static RuliiSystem getInstance() {
        return instance;
    }

    private void init() {
        this.configuration = RuliiConfigurationBuilder.create().build();
    }

    public RuliiConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(RuliiConfiguration configuration) {
        this.configuration = configuration;
    }

    public BindingMatchingStrategy getMatchingStrategy() {
        return getConfiguration().getMatchingStrategy();
    }

    public void setMatchingStrategy(BindingMatchingStrategy matchingStrategy) {
        getConfiguration().setMatchingStrategy(matchingStrategy);
    }

    public ParameterResolver getParameterResolver() {
        return getConfiguration().getParameterResolver();
    }

    public void setParameterResolver(ParameterResolver parameterResolver) {
        getConfiguration().setParameterResolver(parameterResolver);
    }

    public MethodResolver getMethodResolver() {
        return getConfiguration().getMethodResolver();
    }

    public void setMethodResolver(MethodResolver methodResolver) {
        getConfiguration().setMethodResolver(methodResolver);
    }

    public MessageFormatter getMessageFormatter() {
        return getConfiguration().getMessageFormatter();
    }

    public void setMessageFormatter(MessageFormatter messageFormatter) {
        getConfiguration().setMessageFormatter(messageFormatter);
    }

    public ConverterRegistry getConverterRegistry() {
        return getConfiguration().getConverterRegistry();
    }

    public void setConverterRegistry(ConverterRegistry converterRegistry) {
        getConfiguration().setConverterRegistry(converterRegistry);
    }

    public ObjectFactory getObjectFactory() {
        return getConfiguration().getObjectFactory();
    }

    public void setObjectFactory(ObjectFactory objectFactory) {
        getConfiguration().setObjectFactory(objectFactory);
    }

    public Clock getClock() {
        return getConfiguration().getClock();
    }

    public void setClock(Clock clock) {
        getConfiguration().setClock(clock);
    }

    public Locale getLocale() {
        return getConfiguration().getLocale();
    }

    public void setLocale(Locale locale) {
        getConfiguration().setLocale(locale);
    }

    public MessageResolver getMessageResolver() {
        return getConfiguration().getMessageResolver();
    }

    public void setMessageResolver(MessageResolver messageResolver) {
        getConfiguration().setMessageResolver(messageResolver);
    }

    public ScriptProcessor getScriptProcessor() {
        return getConfiguration().getScriptProcessor();
    }

    public void setScriptProcessor(ScriptProcessor scriptProcessor) {
        getConfiguration().setScriptProcessor(scriptProcessor);
    }

    @Override
    public String toString() {
        return "RuliiSystem{" +
                "configuration=" + configuration +
                '}';
    }
}
