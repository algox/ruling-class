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
package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleContextBuilder;
import org.algorithmx.rules.event.ConditionExecution;
import org.algorithmx.rules.event.EventType;
import org.algorithmx.rules.event.ExecutionEvent;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.function.Predicate;

/**
 * Given Condition definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Condition extends Predicate<Object[]> {

    /**
     * Derives all the arguments and executes this Condition.
     *
     * @param ctx Rule Context.
     * @return result of the function.
     * @throws ConditionExecutionException thrown if there are any errors during the Condition execution.
     */
    default boolean isPass(RuleContext ctx) throws ConditionExecutionException {
        Assert.notNull(ctx, "ctx cannot be null.");

        ParameterMatch[] matches = null;
        Object[] values = null;
        ExecutionEvent<ConditionExecution> event = null;

        try {
            matches = ctx.match(getMethodDefinition());
            values = ctx.resolve(matches, getMethodDefinition());
            boolean result = isPass(values);
            event = new ExecutionEvent(EventType.ON_CONDITION,
                    new ConditionExecution(this, result, getMethodDefinition(), RuleUtils.immutable(matches), values));
            return result;
        } catch (Exception e) {
            event = new ExecutionEvent(EventType.ON_CONDITION,
                    new ConditionExecution(this, e, getMethodDefinition(), RuleUtils.immutable(matches), values));
            throw new ConditionExecutionException("Unexpected error occurred trying to execute Condition.",
                    e, this, matches, values);
        } finally {
            if (event != null) ctx.fireListeners(event);
        }
    }

    /**
     * Executes the Function given all the arguments it needs.
     *
     * @param params parameters in order.
     * @return result of the function.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    boolean isPass(Object...params) throws UnrulyException;

    /**
     * Derives all the arguments and executes this Condition.
     *
     * @param params Condition Parameters.
     * @return true if the Condition passed; false otherwise.
     * @throws ConditionExecutionException thrown if there are any errors during the Condition execution.
     */
    default boolean isPass(BindingDeclaration...params) throws ConditionExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return isPass(RuleContextBuilder.create(bindings));
    }

    /**
     * Executes Condition given all the arguments it needs.
     *
     * @param args Rule Condition args in necessary order.
     * @return true if the Rule Condition is true; false otherwise.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default boolean test(Object...args) throws UnrulyException {
        return isPass(args);
    }

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
}
