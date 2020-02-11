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
 * Convenient way to create ActionConsumer Actions (ie. Lambda creation).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Deprecated
public final class Actions {

    private Actions() {
        super();
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with no parameters) is converted into an Action.
     *
     * @param action desired action.
     * @return new ActionConsumer Action with no parameters.
     */
    public static ActionConsumer.ActionConsumer0 act0(ActionConsumer.ActionConsumer0 action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with one parameter) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return new ActionConsumer Action with one parameter.
     */
    public static <A> ActionConsumer.ActionConsumer1 act1(ActionConsumer.ActionConsumer1<A> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with two parameter) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new ActionConsumer Action with two parameter.
     */
    public static <A, B> ActionConsumer.ActionConsumer2 act2(ActionConsumer.ActionConsumer2<A, B> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with three parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new ActionConsumer Action with three parameter.
     */
    public static <A, B, C> ActionConsumer.ActionConsumer3 act3(ActionConsumer.ActionConsumer3<A, B, C> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with four parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new ActionConsumer Action with four parameter.
     */
    public static <A, B, C, D> ActionConsumer.ActionConsumer4 act4(ActionConsumer.ActionConsumer4<A, B, C, D> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with five parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new ActionConsumer Action with five parameter.
     */
    public static <A, B, C, D, E> ActionConsumer.ActionConsumer5 act5(ActionConsumer.ActionConsumer5<A, B, C, D, E> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with six parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new ActionConsumer Action with six parameter.
     */
    public static <A, B, C, D, E, F> ActionConsumer.ActionConsumer6 act6(ActionConsumer.ActionConsumer6<A, B, C, D, E, F> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with seven parameters) is converted into an Action.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new ActionConsumer Action with seven parameter.
     */
    public static <A, B, C, D, E, F, G> ActionConsumer.ActionConsumer7 act7(ActionConsumer.ActionConsumer7<A, B, C, D, E, F, G> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with eight parameters) is converted into an Action.
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
     * @return new ActionConsumer Action with eight parameter.
     */
    public static <A, B, C, D, E, F, G, H> ActionConsumer.ActionConsumer8 act8(ActionConsumer.ActionConsumer8<A, B, C, D, E, F, G, H> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with nine parameters) is converted into an Action.
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
     * @return new ActionConsumer Action with nine parameter.
     */
    public static <A, B, C, D, E, F, G, H, I> ActionConsumer.ActionConsumer9 act9(ActionConsumer.ActionConsumer9<A, B, C, D, E, F, G, H, I> action) {
        return action;
    }

    /**
     * Creates a new ActionConsumer Action. The Lambda (with ten parameters) is converted into an Action.
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
     * @return new ActionConsumer Action with ten parameter.
     */
    public static <A, B, C, D, E, F, G, H, I, J> ActionConsumer.ActionConsumer10 act10(ActionConsumer.ActionConsumer10<A, B, C, D, E, F, G, H, I, J> action) {
        return action;
    }

}
