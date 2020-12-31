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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;

import java.util.function.Predicate;

/**
 * Given Condition definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Condition extends SimpleCondition, Predicate<Object[]> {

    /**
     * Executes the Function given all the arguments it needs.
     *
     * @param params parameters in order.
     * @return result of the function.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    boolean isTrue(Object...params) throws UnrulyException;

    /**
     * Executes Condition given all the arguments it needs.
     *
     * @param args Rule Condition args in necessary order.
     * @return true if the Rule Condition is true; false otherwise.
     * @throws UnrulyException thrown if there are any runtime errors during the execution.
     */
    default boolean test(Object...args) throws UnrulyException {
        return isTrue(args);
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
