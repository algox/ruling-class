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

import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.bind.match.ParameterResolver;
import org.algorithmx.rulii.convert.ConverterRegistry;
import org.algorithmx.rulii.core.registry.RuleRegistry;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.text.MessageFormatter;
import org.algorithmx.rulii.text.MessageResolver;
import org.algorithmx.rulii.util.reflect.MethodResolver;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.validation.extract.ExtractorRegistry;

import java.time.Clock;
import java.util.Locale;

public class RuliiConfiguration {

    private BindingMatchingStrategy matchingStrategy;
    private ParameterResolver parameterResolver;
    private MessageResolver messageResolver;
    private MethodResolver methodResolver;
    private MessageFormatter messageFormatter;
    private ConverterRegistry converterRegistry;
    private ExtractorRegistry extractorRegistry;
    private RuleRegistry ruleRegistry;
    private String scriptLanguage;
    private ObjectFactory objectFactory;
    private Clock clock;
    private Locale locale;

    public RuliiConfiguration(BindingMatchingStrategy matchingStrategy, ParameterResolver parameterResolver,
                              MethodResolver methodResolver, MessageResolver messageResolver,
                              MessageFormatter messageFormatter, ConverterRegistry converterRegistry,
                              ExtractorRegistry extractorRegistry, RuleRegistry ruleRegistry,
                              ObjectFactory objectFactory, String scriptLanguage,
                              Clock clock, Locale locale) {
        super();
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null.");
        Assert.notNull(parameterResolver, "parameterResolver cannot be null.");
        Assert.notNull(methodResolver, "methodResolver cannot be null.");
        Assert.notNull(messageResolver, "messageResolver cannot be null.");
        Assert.notNull(messageFormatter, "messageFormatter cannot be null.");
        Assert.notNull(converterRegistry, "converterRegistry cannot be null.");
        Assert.notNull(extractorRegistry, "extractorRegistry cannot be null.");
        Assert.notNull(ruleRegistry, "ruleRegistry cannot be null.");
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        Assert.notNull(clock, "clock cannot be null.");
        Assert.notNull(locale, "locale cannot be null.");
        this.matchingStrategy = matchingStrategy;
        this.parameterResolver = parameterResolver;
        this.methodResolver = methodResolver;
        this.messageResolver = messageResolver;
        this.messageFormatter = messageFormatter;
        this.converterRegistry = converterRegistry;
        this.extractorRegistry = extractorRegistry;
        this.ruleRegistry = ruleRegistry;
        this.objectFactory = objectFactory;
        this.clock = clock;
        this.locale = locale;
        this.scriptLanguage = scriptLanguage;
    }

    public BindingMatchingStrategy getMatchingStrategy() {
        return matchingStrategy;
    }

    public ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    public MethodResolver getMethodResolver() {
        return methodResolver;
    }

    public MessageFormatter getMessageFormatter() {
        return messageFormatter;
    }

    public ConverterRegistry getConverterRegistry() {
        return converterRegistry;
    }

    public ExtractorRegistry getExtractorRegistry() {
        return extractorRegistry;
    }

    public RuleRegistry getRuleRegistry() {
        return ruleRegistry;
    }

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public Clock getClock() {
        return clock;
    }

    public Locale getLocale() {
        return locale;
    }

    public MessageResolver getMessageResolver() {
        return messageResolver;
    }

    public String getScriptLanguage() {
        return scriptLanguage;
    }

    void setScriptLanguage(String scriptLanguage) {
        this.scriptLanguage = scriptLanguage;
    }

    void setMatchingStrategy(BindingMatchingStrategy matchingStrategy) {
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null.");
        this.matchingStrategy = matchingStrategy;
    }

    void setParameterResolver(ParameterResolver parameterResolver) {
        Assert.notNull(parameterResolver, "parameterResolver cannot be null.");
        this.parameterResolver = parameterResolver;
    }

    void setMessageResolver(MessageResolver messageResolver) {
        Assert.notNull(messageResolver, "messageResolver cannot be null.");
        this.messageResolver = messageResolver;
    }

    void setMethodResolver(MethodResolver methodResolver) {
        Assert.notNull(methodResolver, "methodResolver cannot be null.");
        this.methodResolver = methodResolver;
    }

    void setMessageFormatter(MessageFormatter messageFormatter) {
        Assert.notNull(messageFormatter, "messageFormatter cannot be null.");
        this.messageFormatter = messageFormatter;
    }

    void setConverterRegistry(ConverterRegistry converterRegistry) {
        Assert.notNull(converterRegistry, "converterRegistry cannot be null.");
        this.converterRegistry = converterRegistry;
    }

    void setExtractorRegistry(ExtractorRegistry extractorRegistry) {
        Assert.notNull(extractorRegistry, "extractorRegistry cannot be null.");
        this.extractorRegistry = extractorRegistry;
    }

    void setRuleRegistry(RuleRegistry ruleRegistry) {
        this.ruleRegistry = ruleRegistry;
    }

    void setObjectFactory(ObjectFactory objectFactory) {
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
    }

    void setClock(Clock clock) {
        Assert.notNull(clock, "clock cannot be null.");
        this.clock = clock;
    }

    void setLocale(Locale locale) {
        Assert.notNull(locale, "locale cannot be null.");
        this.locale = locale;
    }

    @Override
    public String toString() {
        return "RuliiConfiguration{" +
                "matchingStrategy=" + matchingStrategy +
                ", parameterResolver=" + parameterResolver +
                ", messageResolver=" + messageResolver +
                ", methodResolver=" + methodResolver +
                ", messageFormatter=" + messageFormatter +
                ", converterRegistry=" + converterRegistry +
                ", extractorRegistry=" + extractorRegistry +
                ", scriptLanguage=" + scriptLanguage +
                ", objectFactory=" + objectFactory +
                ", clock=" + clock +
                ", locale=" + locale +
                '}';
    }
}
