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
 * Convenient way to create Then Actions (ie. Lambda creation).
 *
 * @author Max Arulananthan
 * @since 1.0
 */

public final class Actions {

    private Actions() {
        super();
    }

    /**
     * Creates a new Then Action. The Lambda (with no parameters) is converted into an Action.
     *
     * @param action desired action.
     * @return new Then Action with no parameters.
     */
    public static Then.Then0 args0(Then.Then0 action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with one parameter) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return new Then Action with one parameter.
     */
    public static <A> Then.Then1 args1(Then.Then1<A> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with two parameter) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new Then Action with two parameter.
     */
    public static <A, B> Then.Then2 args2(Then.Then2<A, B> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with three parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new Then Action with three parameter.
     */
    public static <A, B, C> Then.Then3 args3(Then.Then3<A, B, C> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with four parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new Then Action with four parameter.
     */
    public static <A, B, C, D> Then.Then4 args4(Then.Then4<A, B, C, D> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with five parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new Then Action with five parameter.
     */
    public static <A, B, C, D, E> Then.Then5 args5(Then.Then5<A, B, C, D, E> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with six parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new Then Action with six parameter.
     */
    public static <A, B, C, D, E, F> Then.Then6 args6(Then.Then6<A, B, C, D, E, F> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with seven parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new Then Action with seven parameter.
     */
    public static <A, B, C, D, E, F, G> Then.Then7 args7(Then.Then7<A, B, C, D, E, F, G> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with eight parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @return new Then Action with eight parameter.
     */
    public static <A, B, C, D, E, F, G, H> Then.Then8 args8(Then.Then8<A, B, C, D, E, F, G, H> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with nine parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @return new Then Action with nine parameter.
     */
    public static <A, B, C, D, E, F, G, H, I> Then.Then9 args9(Then.Then9<A, B, C, D, E, F, G, H, I> action) {
        return action;
    }

    /**
     * Creates a new Then Action. The Lambda (with ten parameters) is converted into an Action.
     *
     * @param action desired action.
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
     * @return new Then Action with ten parameter.
     */
    public static <A, B, C, D, E, F, G, H, I, J> Then.Then10 args10(Then.Then10<A, B, C, D, E, F, G, H, I, J> action) {
        return action;
    }

}
