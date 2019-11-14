/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ConditionDefinition;

/**
 * Given Condition definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Condition  {

    /**
     * Derives all the arguments and executed this Condition.
     *
     * @param ctx Rule Context.
     * @return true if the Condition passed; false otherwise.
     * @throws UnrulyException thrown if there are any errors during the Condition execution.
     */
    default boolean isPass(RuleContext ctx) throws UnrulyException {
        ParameterResolver.ParameterMatch[] matches = ctx.getParameterResolver().resolveAsBindings(
                getConditionDefinition().getMethodDefinition(), ctx.getBindings(), ctx.getMatchingStrategy());
        return isPass(ctx.getParameterResolver().resolveAsBindingValues(matches));
    }

    /**
     * Executes the Condition given all the arguments it needs.
     *
     * @param params Condition parameters in necessary order.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     * @return true if the condition is met; false otherwise.
     */
    boolean isPass(Object...params) throws UnrulyException;

    /**
     * Meta information about the Condition.
     *
     * @return Condition meta information.
     */
    ConditionDefinition getConditionDefinition();

    /**
     * The actual target instance the Condition is associated to (usually a Rule).
     *
     * @return target instance.
     */
    Object getTarget();
}
