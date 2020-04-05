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

import org.algorithmx.rules.core.UnrulyException;

/**
 * Functional Action taking in two parameters.
 *
 * @param <A> generic type of the 1st parameter.
 * @param <B> generic type of the 2nd parameter.
 * @param <B> generic type of the 3rd parameter.
 * @param <B> generic type of the 4th parameter.
 * @param <B> generic type of the 5th parameter.
 * @param <B> generic type of the 6th parameter.
 * @param <B> generic type of the 7th parameter.
 * @param <B> generic type of the 8th parameter.
 * @param <B> generic type of the 9th parameter.
 * @param <B> generic type of the 10th parameter.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@FunctionalInterface
public interface DecAction<A, B, C, D, E, F, G, H, I, J> extends FunctionalAction {

    /**
     * Action logic taking in ten args.
     *
     * @param arg0 1st arg.
     * @param arg1 2nd arg.
     * @param arg2 2nd arg.
     * @param arg3 2nd arg.
     * @param arg4 2nd arg.
     * @param arg5 2nd arg.
     * @param arg6 2nd arg.
     * @param arg7 2nd arg.
     * @param arg8 2nd arg.
     * @param arg9 2nd arg.
     */
    void execute(A arg0, B arg1, C arg2, D arg3, E arg4, F arg5, G arg6, H arg7, I arg8, J arg9);

    @Override
    default void execute(Object...params) throws UnrulyException {
        int expected = 10;

        if (params == null || params.length != expected) {
            throw new UnrulyException("Invalid number of params. Expected " + expected + "] provided ["
                    + (params == null ? 0 : params.length) + "]");
        }
        
        execute((A) params[0], (B) params[1], (C) params[2], (D) params[3], (E) params[4], (F) params[5],
                (G) params[6], (H) params[7], (I) params[8], (J) params[9]);
    }
}
