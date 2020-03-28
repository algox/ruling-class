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

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.spring.util.Assert;

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
    private final BindableMethodExecutor methodExecutor;

    private RuleExecutionState state = RuleExecutionState.RUNNING;

    public RuleContext(Bindings bindings) {
        this(bindings, BindingMatchingStrategy.getDefault(), ParameterResolver.defaultParameterResolver(),
                BindableMethodExecutor.defaultBindableMethodExecutor());
    }

    public RuleContext(Bindings bindings, BindingMatchingStrategy matchingStrategy,
                       ParameterResolver parameterResolver, BindableMethodExecutor methodExecutor) {
        super();
        Assert.notNull(bindings, "bindings cannot be null.");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null.");
        Assert.notNull(parameterResolver, "parameterResolver cannot be null.");
        Assert.notNull(methodExecutor, "methodExecutor cannot be null.");
        this.bindings = bindings;
        this.matchingStrategy = matchingStrategy;
        this.parameterResolver = parameterResolver;
        this.methodExecutor = methodExecutor;
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
     * Returns the parameter resolver being used.
     *
     * @return parameter resolver. Cannot be null.
     */
    public ParameterResolver getParameterResolver() {
        return parameterResolver;
    }

    public BindableMethodExecutor getMethodExecutor() {
        return methodExecutor;
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
