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

import java.io.Serializable;

public interface Then extends Serializable {

    long serialVersionUID = -0L;

    @FunctionalInterface
    interface Then0 extends Then {
        void then();
    }

    @FunctionalInterface
    interface Then1<A> extends Then {
        void then(A arg1);
    }

    @FunctionalInterface
    interface Then2<A, B> extends Then {
        void then(A arg1, B arg2);
    }

    @FunctionalInterface
    interface Then3<A, B, C> extends Then {
        void then(A arg1, B arg2, C arg3);
    }

    @FunctionalInterface
    interface Then4<A, B, C, D> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4);
    }

    @FunctionalInterface
    interface Then5<A, B, C, D, E> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4, E arg5);
    }

    @FunctionalInterface
    interface Then6<A, B, C, D, E, F> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6);
    }

    @FunctionalInterface
    interface Then7<A, B, C, D, E, F, G> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7);
    }

    @FunctionalInterface
    interface Then8<A, B, C, D, E, F, G, H> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8);
    }

    @FunctionalInterface
    interface Then9<A, B, C, D, E, F, G, H, I> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9);
    }

    @FunctionalInterface
    interface Then10<A, B, C, D, E, F, G, H, I, J> extends Then {
        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9, J arg10);
    }
}
