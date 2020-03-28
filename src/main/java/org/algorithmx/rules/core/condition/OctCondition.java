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

@FunctionalInterface
public interface OctCondition<A, B, C, D, E, F, G, H> extends FunctionalCondition {

    @Override
    default boolean isPass(Object...params) throws UnrulyException {
        int expected = 8;

        if (params == null || params.length != expected) {
            throw new UnrulyException("Invalid number of params. Expected " + expected + "] provided ["
                    + (params == null ? 0 : params.length) + "]");
        }
        
        return when((A) params[0], (B) params[1], (C) params[2], (D) params[3], (E) params[4], (F) params[5],
                (G) params[6], (H) params[7]);
    }

    boolean when(A arg0, B arg1, C arg2, D arg3, E arg4, F args5, G args6, H args7);
}
