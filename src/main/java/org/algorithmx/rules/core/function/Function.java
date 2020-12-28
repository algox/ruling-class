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
package org.algorithmx.rules.core.function;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.event.FunctionExecution;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

/**
 * Represents a function that accepts argument(s) and produces a result.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Function<T> extends Comparable<Function> {

    /**
     * Derives all the arguments and executes this Function.
     *
     * @param ctx Rule Context.
     * @return result of the function.
     * @throws UnrulyException thrown if there are any errors during the Function execution.
     */
    default T apply(RuleContext ctx) throws UnrulyException {
        Assert.notNull(ctx, "ctx cannot be null.");

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<FunctionExecution> event = null;

        try {
            matches = ctx.match(getMethodDefinition());
            values = ctx.resolve(matches, getMethodDefinition());
            T result = apply(values);
            event = new ExecutionEvent(this instanceof Condition ? EventType.ON_CONDITION : EventType.ON_FUNCTION,
                    new FunctionExecution(this, result, getMethodDefinition(), matches, values));
            return result;
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            event = new ExecutionEvent(this instanceof Condition ? EventType.ON_CONDITION : EventType.ON_FUNCTION,
                    new FunctionExecution(this, e, getMethodDefinition(), matches, values));
            throw new UnrulyException("Unexpected error occurred trying to execute " + getClass().getSimpleName() + "."
                    + System.lineSeparator()
                    + RuleUtils.getMethodDescription(getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    /**
     * Executes thr Function given all the arguments it needs.
     *
     * @param params parameters in order.
     * @return result of the function.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    T apply(Object ... params) throws UnrulyException;

    /**
     * Meta information about the Function.
     *
     * @return Function meta information.
     */
    MethodDefinition getMethodDefinition();

    /**
     * The actual target instance the Function is associated to.
     *
     * @return target instance.
     */
    Object getTarget();

    @Override
    default int compareTo(Function other) {
        return getMethodDefinition().compareTo(other.getMethodDefinition());
    }
}
