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

package org.algorithmx.rulii.core.context;

import org.algorithmx.rulii.bind.BindingBuilder;
import org.algorithmx.rulii.bind.BindingDeclaration;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.DefaultBindings;
import org.algorithmx.rulii.bind.ReservedBindings;
import org.algorithmx.rulii.bind.ScopedBindings;
import org.algorithmx.rulii.bind.convert.ConverterRegistry;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategyType;
import org.algorithmx.rulii.bind.match.ParameterResolver;
import org.algorithmx.rulii.event.EventProcessor;
import org.algorithmx.rulii.event.ExecutionListener;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.script.ScriptLanguageManager;
import org.algorithmx.rulii.text.MessageFormatter;
import org.algorithmx.rulii.text.MessageResolver;
import org.algorithmx.rulii.util.SystemDefaults;
import org.algorithmx.rulii.util.SystemDefaultsHolder;
import org.algorithmx.rulii.util.reflect.ObjectFactory;

import java.time.Clock;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Builder class to properly build a RuleContext with the bells and whistles.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContextBuilder {

    private final Bindings bindings;
    private final SystemDefaults defaults;

    private BindingMatchingStrategy matchingStrategy;
    private ParameterResolver parameterResolver;
    private MessageResolver messageResolver;
    private MessageFormatter messageFormatter;
    private ObjectFactory objectFactory;
    private EventProcessor eventProcessor;
    private ConverterRegistry registry;
    private String scriptingLanguage;
    private Clock clock;
    private Locale locale;
    private List<ExecutionListener> listeners = new ArrayList<>();

    private RuleContextBuilder(Bindings bindings) {
        super();
        this.bindings = bindings;
        this.defaults = SystemDefaultsHolder.getInstance().getDefaults();
        init();
    }

    protected void init() {
        this.matchingStrategy = defaults.getBindingMatchingStrategy();
        this.parameterResolver = defaults.getParameterResolver();
        this.messageResolver = defaults.getMessageResolver();
        this.messageFormatter = defaults.getMessageFormatter();
        this.objectFactory = defaults.getObjectFactory();
        this.eventProcessor = EventProcessor.create();
        this.registry = defaults.getConverterRegistry();
        this.clock = defaults.getClock();
        this.locale = defaults.getDefaultLocale();
        this.scriptingLanguage = defaults.getScriptingLanguage();

        // The default language not present; lets pick first one(alphabetically) that is avail.
        if (ScriptLanguageManager.getScriptProcessor(defaults.getScriptingLanguage()) == null
                && ScriptLanguageManager.getAvailableScriptingLanguages().length > 0) {
            this.scriptingLanguage = ScriptLanguageManager.getAvailableScriptingLanguages()[0];
        }
    }

    /**
     * Sets the Bindings to use.
     *
     * @param bindings Bindings to use.
     * @return this for fluency.
     */
    public static RuleContextBuilder with(Bindings bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        return new RuleContextBuilder(bindings);
    }

    public static RuleContextBuilder with(BindingDeclaration...params) {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return with(bindings);
    }

    public static RuleContext build(Bindings bindings) {
        return with(bindings).build();
    }

    public static RuleContext build(BindingDeclaration...params) {
        return with(params).build();
    }

    /**
     * Sets the matching strategy to uss.
     *
     * @param type matching strategy type.
     * @return this for fluency.
     */
    public RuleContextBuilder matchUsing(BindingMatchingStrategyType type) {
        Assert.notNull(type, "type cannot be null.");
        this.matchingStrategy = type.getStrategy();
        return this;
    }

    public RuleContextBuilder paramResolver(ParameterResolver parameterResolver) {
        Assert.notNull(objectFactory, "parameterResolver cannot be null.");
        this.parameterResolver = parameterResolver;
        return this;
    }

    public RuleContextBuilder messageResolver(MessageResolver messageResolver) {
        Assert.notNull(messageResolver, "messageResolver cannot be null.");
        this.messageResolver = messageResolver;
        return this;
    }

    public RuleContextBuilder messageResolver(String...baseNames) {
        Assert.notNull(baseNames, "baseNames cannot be null.");
        this.messageResolver = MessageResolver.create(baseNames);
        return this;
    }

    public RuleContextBuilder messageFormatter(MessageFormatter messageFormatter) {
        Assert.notNull(messageFormatter, "messageFormatter cannot be null.");
        this.messageFormatter = messageFormatter;
        return this;
    }

    public RuleContextBuilder objectFactory(ObjectFactory objectFactory) {
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
        return this;
    }

    public RuleContextBuilder eventProcessor(EventProcessor eventProcessor) {
        Assert.notNull(eventProcessor, "eventProcessor cannot be null.");
        this.eventProcessor = eventProcessor;
        return this;
    }

    public RuleContextBuilder traceUsing(ExecutionListener listener) {
        Assert.notNull(listener, "listener cannot be null.");
        this.listeners.add(listener);
        return this;
    }

    public RuleContextBuilder converterRegistry(ConverterRegistry registry) {
        Assert.notNull(registry, "registry cannot be null.");
        this.registry = registry;
        return this;
    }

    public RuleContextBuilder scriptProcessor(String language) {
        Assert.notNull(language, "language cannot be null.");
        this.scriptingLanguage = language;
        return this;
    }

    public RuleContextBuilder locale(Locale locale) {
        Assert.notNull(locale, "locale cannot be null.");
        this.locale = locale;
        return this;
    }

    public RuleContextBuilder clock(Clock clock) {
        Assert.notNull(clock, "clock cannot be null.");
        this.clock = clock;
        return this;
    }

    /**
     * Builds a Rule Context with desired parameters.
     *
     * @return new Rule Context.
     */
    public RuleContext build() {
        ScopedBindings scopedBindings = ScopedBindings.create();

        RuleContext result  = new RuleContext(scopedBindings, locale, matchingStrategy, parameterResolver, messageResolver,
                messageFormatter, objectFactory, eventProcessor, registry, scriptingLanguage, clock);
        // Make the Context avail in the bindings.
        ((DefaultBindings) (scopedBindings.getRootScope())).promiscuousBind(BindingBuilder
                .with(ReservedBindings.RULE_CONTEXT.getName())
                .type(RuleContext.class)
                .value(result)
                .build());
        scopedBindings.addScope(ScopedBindings.GLOBAL_SCOPE, bindings);
        listeners.stream().forEach(listener -> result.getEventProcessor().addEventListener(listener));

        return result;
    }
}
