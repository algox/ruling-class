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

import org.algorithmx.rules.error.UnrulyException;

/**
 * Functional Condition taking in four parameters.
 *
 * @param <A> generic type of the 1st parameter.
 * @param <B> generic type of the 2nd parameter.
 * @param <C> generic type of the 3rd parameter.
 * @param <D> generic type of the 4th parameter.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@FunctionalInterface
public interface QuadCondition<A, B, C, D> extends FunctionalCondition {

    /**
     * Condition logic taking in four args.
     *
     * @param arg0 1st arg.
     * @param arg1 2nd arg.
     * @param arg2 3rd arg.
     * @param arg3 4th arg.
     *
     * @return true if the condition is met; false otherwise.
     */
    boolean when(A arg0, B arg1, C arg2, D arg3);

    @Override
    default boolean isPass(Object...args) throws UnrulyException {
        int expected = 4;

        if (args == null || args.length != expected) {
            throw new UnrulyException("Invalid number of args. Expected " + expected + "] provided ["
                    + (args == null ? 0 : args.length) + "]");
        }
        
        return when((A) args[0], (B) args[1], (C) args[2], (D) args[3]);
    }
}
