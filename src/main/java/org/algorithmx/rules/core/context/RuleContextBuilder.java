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
package org.algorithmx.rules.core.context;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ScopedBindings;
import org.algorithmx.rules.bind.convert.ConverterRegistry;
import org.algorithmx.rules.bind.match.BindingMatchingStrategy;
import org.algorithmx.rules.bind.match.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.match.ParameterResolver;
import org.algorithmx.rules.event.EventProcessor;
import org.algorithmx.rules.event.ExecutionListener;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.script.NoOpScriptProcessor;
import org.algorithmx.rules.script.ScriptProcessor;
import org.algorithmx.rules.text.MessageFormatter;
import org.algorithmx.rules.text.MessageResolver;
import org.algorithmx.rules.util.reflect.ObjectFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class to properly build a RuleContext with the bells and whistles.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContextBuilder {

    private final Bindings bindings;
    private BindingMatchingStrategy matchingStrategy = BindingMatchingStrategy.create();
    private ParameterResolver parameterResolver = ParameterResolver.create();
    private MessageResolver messageResolver = MessageResolver.create("rules");
    private MessageFormatter messageFormatter = MessageFormatter.create();
    private ObjectFactory objectFactory = ObjectFactory.create();
    private EventProcessor eventProcessor = EventProcessor.create();
    private ConverterRegistry registry = ConverterRegistry.create();
    private String ruleContextBindingName = "ruleContext";
    private ScriptProcessor scriptProcessor = null;
    private List<ExecutionListener> listeners = new ArrayList<>();

    private RuleContextBuilder(Bindings bindings) {
        super();
        this.bindings = bindings;
        setScriptProcessor();
    }

    protected void setScriptProcessor() {
        try {
            this.scriptProcessor = ScriptProcessor.create();
        } catch (Exception e) {
            this.scriptProcessor = new NoOpScriptProcessor();
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

    public static RuleContext create(Bindings bindings) {
        return with(bindings).build();
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

    public RuleContextBuilder converterRegistry(ConverterRegistry registry) {
        Assert.notNull(registry, "registry cannot be null.");
        this.registry = registry;
        return this;
    }

    public RuleContextBuilder ruleContextBindingName(String name) {
        Assert.notNull(name, "name cannot be null.");
        this.ruleContextBindingName = name;
        return this;
    }

    public RuleContextBuilder traceUsing(ExecutionListener listener) {
        Assert.notNull(listener, "listener cannot be null.");
        this.listeners.add(listener);
        return this;
    }

    public RuleContextBuilder scriptProcessor(ScriptProcessor scriptProcessor) {
        Assert.notNull(scriptProcessor, "scriptProcessor cannot be null.");
        this.scriptProcessor = scriptProcessor;
        return this;
    }

    /**
     * Builds a Rule Context with desired parameters.
     *
     * @return new Rule Context.
     */
    public RuleContext build() {
        ScopedBindings scopedBindings = ScopedBindings.create(bindings);
        Bindings contextScope = scopedBindings.addScope();

        RuleContext result  = new RuleContext(scopedBindings, matchingStrategy, parameterResolver, messageResolver,
                messageFormatter, objectFactory, eventProcessor, registry, scriptProcessor);
        contextScope.bind(ruleContextBindingName, RuleContext.class, result);
        listeners.stream().forEach(listener -> result.getEventProcessor().addEventListener(listener));

        return result;
    }
}
