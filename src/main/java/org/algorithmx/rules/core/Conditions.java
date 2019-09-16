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

/**
 * Convenient way to create Conditions (ie. Lambda creation).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class Conditions {

    private Conditions() {
        super();
    }

    /**
     * Creates a new condition with no arguments.
     *
     * @param condition desired condition.
     * @return new Condition with no arguments.
     */
    public static Condition.Condition0 cond0(Condition.Condition0 condition) {
        return condition;
    }

    /**
     * Creates a new condition with one argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @return new Condition with one arguments.
     */
    public static <A> Condition.Condition1<A> cond1(Condition.Condition1<A> condition) {
        return condition;
    }

    /**
     * Creates a new condition with two argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new Condition with two arguments.
     */
    public static <A, B> Condition.Condition2<A, B> cond2(Condition.Condition2<A, B> condition) {
        return condition;
    }

    /**
     * Creates a new condition with three argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new Condition with three arguments.
     */
    public static <A, B, C> Condition.Condition3<A, B, C> cond3(Condition.Condition3<A, B, C> condition) {
        return condition;
    }

    /**
     * Creates a new condition with four argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new Condition with four arguments.
     */
    public static <A, B, C, D> Condition.Condition4<A, B, C, D> cond4(Condition.Condition4<A, B, C, D> condition) {
        return condition;
    }

    /**
     * Creates a new condition with five argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new Condition with five arguments.
     */
    public static <A, B, C, D, E> Condition.Condition5<A, B, C, D, E> cond5(
            Condition.Condition5<A, B, C, D, E> condition) {
        return condition;
    }

    /**
     * Creates a new condition with six argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new Condition with six arguments.
     */
    public static <A, B, C, D, E, F> Condition.Condition6<A, B, C, D, E, F> cond6(
            Condition.Condition6<A, B, C, D, E, F> condition) {
        return condition;
    }

    /**
     * Creates a new condition with seven argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new Condition with seven arguments.
     */
    public static <A, B, C, D, E, F, G> Condition.Condition7<A, B, C, D, E, F, G> cond7(
            Condition.Condition7<A, B, C, D, E, F, G> condition) {
        return condition;
    }

    /**
     * Creates a new condition with eight argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @return new Condition with eight arguments.
     */
    public static <A, B, C, D, E, F, G, H> Condition.Condition8<A, B, C, D, E, F, G, H> cond8(
            Condition.Condition8<A, B, C, D, E, F, G, H> condition) {
        return condition;
    }

    /**
     * Creates a new condition with nine argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @return new Condition with nine arguments.
     */
    public static <A, B, C, D, E, F, G, H, I> Condition.Condition9<A, B, C, D, E, F, G, H, I> cond9(
            Condition.Condition9<A, B, C, D, E, F, G, H, I> condition) {
        return condition;
    }

    /**
     * Creates a new condition with ten argument.
     *
     * @param condition desired condition.
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
     * @return new Condition with ten arguments.
     */
    public static <A, B, C, D, E, F, G, H, I, J> Condition.Condition10<A, B, C, D, E, F, G, H, I, J> cond10(
            Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition) {
        return condition;
    }
}
