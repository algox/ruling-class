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

/**
 * When Condition definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Condition extends Serializable {

    long serialVersionUID = -0L;

    /**
     * When Condition interface taking no parameters.
     */
    @FunctionalInterface
    interface Condition0 extends Condition {

        /**
         * When condition.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when();
    }

    /**
     * When Condition interface taking one parameter.
     * @param <A> generic type of the first parameter.
     */
    @FunctionalInterface
    interface Condition1<A> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1);
    }

    /**
     * When Condition interface taking two parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     */
    @FunctionalInterface
    interface Condition2<A, B> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2);
    }

    /**
     * When Condition interface taking three parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     */
    @FunctionalInterface
    interface Condition3<A, B, C> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3);
    }

    /**
     * When Condition interface taking four parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     */
    @FunctionalInterface
    interface Condition4<A, B, C, D> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4);
    }

    /**
     * When Condition interface taking five parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     */
    @FunctionalInterface
    interface Condition5<A, B, C, D, E> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @param param5 fifth parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4, E param5);
    }

    /**
     * When Condition interface taking six parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     */
    @FunctionalInterface
    interface Condition6<A, B, C, D, E, F> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @param param5 fifth parameter.
         * @param param6 sixth parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4, E param5, F param6);
    }

    /**
     * When Condition interface taking seven parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     */
    @FunctionalInterface
    interface Condition7<A, B, C, D, E, F, G> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @param param5 fifth parameter.
         * @param param6 sixth parameter.
         * @param param7 seventh parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4, E param5, F param6, G param7);
    }

    /**
     * When Condition interface taking eight parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     */
    @FunctionalInterface
    interface Condition8<A, B, C, D, E, F, G, H> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @param param5 fifth parameter.
         * @param param6 sixth parameter.
         * @param param7 seventh parameter.
         * @param param8 eight parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4, E param5, F param6, G param7, H param8);
    }

    /**
     * When Condition interface taking nine parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     */
    @FunctionalInterface
    interface Condition9<A, B, C, D, E, F, G, H, I> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @param param5 fifth parameter.
         * @param param6 sixth parameter.
         * @param param7 seventh parameter.
         * @param param8 eight parameter.
         * @param param9 ninth parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4, E param5, F param6, G param7, H param8, I param9);
    }

    /**
     * When Condition interface taking ten parameters.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @param <J> generic type of the ninth parameter.
     */
    @FunctionalInterface
    interface Condition10<A, B, C, D, E, F, G, H, I, J> extends Condition {

        /**
         * When condition.
         * @param param1 first parameter.
         * @param param2 second parameter.
         * @param param3 third parameter.
         * @param param4 fourth parameter.
         * @param param5 fifth parameter.
         * @param param6 sixth parameter.
         * @param param7 seventh parameter.
         * @param param8 eight parameter.
         * @param param9 ninth parameter.
         * @param param10 tenth parameter.
         * @return true if the Condition is met; false otherwise.
         */
        boolean when(A param1, B param2, C param3, D param4, E param5, F param6, G param7, H param8, I param9, J param10);
    }
}
