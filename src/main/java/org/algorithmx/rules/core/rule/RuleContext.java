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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.convert.ConverterRegistry;
import org.algorithmx.rules.bind.match.BindingMatchingStrategy;
import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.bind.match.ParameterResolver;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ObjectFactory;

/**
 * Responsible for state management during Rule execution. This class provides access to everything that is required
 * by the Rule Engine to execute a given set of Rules.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleContext {

    private final Bindings bindings;
    private final BindingMatchingStrategy matchingStrategy;
    private final ParameterResolver parameterResolver;
    private final ObjectFactory objectFactory;
    private final ConverterRegistry registry;

    private RuleExecutionState state = RuleExecutionState.RUNNING;

    public RuleContext(Bindings bindings) {
        this(bindings, BindingMatchingStrategy.create(), ParameterResolver.create(),
                ObjectFactory.create(),
                ConverterRegistry.create());
    }

    public RuleContext(Bindings bindings, BindingMatchingStrategy matchingStrategy,
                       ParameterResolver parameterResolver,
                       ObjectFactory objectFactory, ConverterRegistry registry) {
        super();
        Assert.notNull(bindings, "bindings cannot be null.");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null.");
        Assert.notNull(parameterResolver, "parameterResolver cannot be null.");
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        Assert.notNull(registry, "registry cannot be null.");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
        this.parameterResolver = parameterResolver;
        this.objectFactory = objectFactory;
        this.registry = registry;
    }

    public ParameterMatch[] match(MethodDefinition definition) {
        return getParameterResolver().match(definition, getBindings(), getMatchingStrategy(), getObjectFactory());
    }

    public Object[] resolve(ParameterMatch[] matches, MethodDefinition definition) {
        return getParameterResolver().resolve(matches, definition, getBindings(), matchingStrategy, getRegistry());
    }

                                       /**
     * Returns the Bindings.
     *
     * @return Bindings. Cannot be null.
     */
    public Bindings getBindings() {
        return bindings;
    }

    /**
     * Returns the matching strategy to be used.
     *
     * @return matching strategy (cannot be null).
     */
    public BindingMatchingStrategy getMatchingStrategy() {
        return matchingStrategy;
    }

    /**
     * Returns the Parameter resolver being used.
     *
     * @return parameter resolver. Cannot be null.
     */
    protected ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    /**
     * Returns the ObjectFactory being used.
     *
     * @return Object Factory. cannot be null.
     */
    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    /**
     * Returns the ConverterRegistry being used.
     *
     * @return Converter Registry. Cannot be null.
     */
    public ConverterRegistry getRegistry() {
        return registry;
    }

    /**
     * Set the execution state to RUNNING.
     */
    public void start() {
        this.state = RuleExecutionState.RUNNING;
    }

    /**
     * Determines whether the Execution State is RUNNING.
     *
     * @return true if Execution State is RUNNING; false otherwise.
     */
    public boolean isRunning() {
        return getState().isRunning();
    }

    /**
     * Set the execution state to STOPPED.
     */
    public void stop() {
        this.state = RuleExecutionState.STOPPED;
    }

    /**
     * Set the execution state to FINISHED.
     */
    public void finish() {
        this.state = RuleExecutionState.FINISHED;
    }

    /**
     * Set the execution state to ERROR.
     */
    public void error() {
        this.state = RuleExecutionState.ERROR;
    }

    /**
     * Returns the current execution state of this context.
     *
     * @return current execution state.
     */
    public RuleExecutionState getState() {
        return state;
    }
}
