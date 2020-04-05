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

/**
 * Functional Condition taking in a single parameter.
 *
 * @param <A> generic type of the parameter.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@FunctionalInterface
public interface UnaryCondition<A> extends FunctionalCondition {

    /**
     * Condition logic taking in a single arg.
     *
     * @param arg arg value.
     *
     * @return true if the condition is met; false otherwise.
     */
    boolean isPass(A arg);

    @Override
    default boolean isPass(Object...args) throws UnrulyException {
        int expected = 1;

        if (args == null || args.length != expected) {
            throw new UnrulyException("Invalid number of args. Expected " + expected + " provided ["
                    + (args == null ? 0 : args.length) + "]");
        }

        return isPass((A) args[0]);
    }
}
