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
 * Functional Condition taking in eight parameters.
 *
 * @param <A> generic type of the 1st parameter.
 * @param <B> generic type of the 2nd parameter.
 * @param <C> generic type of the 3rd parameter.
 * @param <D> generic type of the 4th parameter.
 * @param <E> generic type of the 5th parameter.
 * @param <F> generic type of the 6th parameter.
 * @param <G> generic type of the 7th parameter.
 * @param <H> generic type of the 8th parameter.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@FunctionalInterface
public interface OctCondition<A, B, C, D, E, F, G, H> extends FunctionalCondition {

    /**
     * Condition logic taking in eight args.
     *
     * @param arg0 1st arg.
     * @param arg1 2nd arg.
     * @param arg2 3rd arg.
     * @param arg3 4th arg.
     * @param arg4 5th arg.
     * @param arg5 6th arg.
     * @param arg6 7th arg.
     * @param arg7 8th arg.
     *
     * @return true if the condition is met; false otherwise.
     */
    boolean isPass(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7);

    @Override
    default boolean isPass(Object...args) throws UnrulyException {
        int expected = 8;

        if (args == null || args.length != expected) {
            throw new UnrulyException("Invalid number of args. Expected " + expected + "] provided ["
                    + (args == null ? 0 : args.length) + "]");
        }
        
        return isPass((A) args[0], (B) args[1], (C) args[2], (D) args[3], (E) args[4], (F) args[5], (G) args[6], (H) args[7]);
    }
}
