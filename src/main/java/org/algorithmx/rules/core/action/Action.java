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
package org.algorithmx.rules.core.action;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

/**
 * Represents an operation that accepts input arguments and returns no result.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Action extends Comparable<Action> {

    /**
     * Derives all the arguments and executes this Action.
     *
     * @param ctx Rule Context.
     * @throws UnrulyException thrown if there are any errors during the Condition execution.
     */
    default void run(RuleContext ctx) throws UnrulyException {
        Assert.notNull(ctx, "ctx cannot be null.");
        ParameterMatch[] matches = null;
        Object[] values = null;

        try {
            matches = ctx.match(getMethodDefinition());
            values = ctx.resolve(matches, getMethodDefinition());
            run(values);
        } catch (Exception e) {
            Throwable cause = e instanceof UnrulyException && e.getCause() != null ? e.getCause() : e;
            throw new UnrulyException(e.getClass().getSimpleName() + " occurred trying to execute Action."
                    + System.lineSeparator()
                    + RuleUtils.getMethodDescription(getMethodDefinition(), matches, values, RuleUtils.TAB), cause);
        }
    }

    /**
     * Executes thr Action given all the arguments it needs.
     *
     * @param params Action parameters in order.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    void run(Object...params) throws UnrulyException;

    /**
     * Meta information about the Action.
     *
     * @return Action meta information.
     */
    MethodDefinition getMethodDefinition();

    /**
     * The actual target instance the Action is associated to.
     *
     * @return target instance.
     */
    Object getTarget();

    @Override
    default int compareTo(Action other) {
        return getMethodDefinition().compareTo(other.getMethodDefinition());
    }
}
