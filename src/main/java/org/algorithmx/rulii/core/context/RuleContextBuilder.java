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
import org.algorithmx.rulii.bind.match.BindingMatchingStrategy;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategyType;
import org.algorithmx.rulii.bind.match.ParameterResolver;
import org.algorithmx.rulii.config.RuliiConfiguration;
import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.convert.ConverterRegistry;
import org.algorithmx.rulii.core.registry.RuleRegistry;
import org.algorithmx.rulii.event.EventProcessor;
import org.algorithmx.rulii.event.ExecutionListener;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.script.NoOpScriptProcessor;
import org.algorithmx.rulii.script.ScriptLanguageManager;
import org.algorithmx.rulii.script.ScriptProcessor;
import org.algorithmx.rulii.text.MessageFormatter;
import org.algorithmx.rulii.text.MessageResolver;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.validation.extract.ExtractorRegistry;

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

    private Bindings bindings;
    private BindingMatchingStrategy matchingStrategy;
    private ParameterResolver parameterResolver;
    private MessageResolver messageResolver;
    private MessageFormatter messageFormatter;
    private ObjectFactory objectFactory;
    private EventProcessor eventProcessor;
    private ConverterRegistry converterRegistry;
    private ExtractorRegistry extractorRegistry;
    private RuleRegistry ruleRegistry;
    private ScriptProcessor scriptProcessor;
    private Clock clock;
    private Locale locale;
    private List<ExecutionListener> listeners = new ArrayList<>();

    private RuleContextBuilder(RuliiConfiguration configuration) {
        super();
        init(configuration);
    }

    protected void init(RuliiConfiguration configuration) {
        Assert.notNull(configuration, "configuration cannot be null.");
        this.matchingStrategy = configuration.getMatchingStrategy();
        this.parameterResolver = configuration.getParameterResolver();
        this.messageResolver = configuration.getMessageResolver();
        this.messageFormatter = configuration.getMessageFormatter();
        this.objectFactory = configuration.getObjectFactory();
        this.eventProcessor = EventProcessor.create();
        this.converterRegistry = configuration.getConverterRegistry();
        this.extractorRegistry = configuration.getExtractorRegistry();
        this.ruleRegistry = configuration.getRuleRegistry();
        this.clock = configuration.getClock();
        this.locale = configuration.getLocale();
        this.scriptProcessor = ScriptLanguageManager.getScriptProcessor(configuration.getScriptLanguage());

        if (scriptProcessor == null) {
            scriptProcessor = new NoOpScriptProcessor();
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
        RuleContextBuilder result = new RuleContextBuilder(RuliiSystem.getInstance().getConfiguration());
        result.bindings(bindings);
        return result;
    }

    public static RuleContextBuilder with(BindingDeclaration...params) {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return with(bindings);
    }

    public static RuleContext empty() {
        return with(Bindings.create()).build();
    }

    public static RuleContext build(Bindings bindings) {
        return with(bindings).build();
    }

    public static RuleContext build(BindingDeclaration...params) {
        return with(params).build();
    }

    public RuleContextBuilder bindings(Bindings bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        this.bindings = bindings;
        return this;
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

    public RuleContextBuilder converterRegistry(ConverterRegistry converterRegistry) {
        Assert.notNull(converterRegistry, "converterRegistry cannot be null.");
        this.converterRegistry = converterRegistry;
        return this;
    }

    public RuleContextBuilder extractorRegistry(ExtractorRegistry extractorRegistry) {
        Assert.notNull(extractorRegistry, "extractorRegistry cannot be null.");
        this.extractorRegistry = extractorRegistry;
        return this;
    }

    public RuleContextBuilder ruleRegistry(RuleRegistry ruleRegistry) {
        Assert.notNull(ruleRegistry, "ruleRegistry cannot be null.");
        this.ruleRegistry = ruleRegistry;
        return this;
    }

    public RuleContextBuilder scriptProcessor(ScriptProcessor scriptProcessor) {
        Assert.notNull(scriptProcessor, "scriptProcessor cannot be null.");
        this.scriptProcessor = scriptProcessor;
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
                messageFormatter, objectFactory, eventProcessor, converterRegistry, extractorRegistry,
                ruleRegistry, scriptProcessor, clock);
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
