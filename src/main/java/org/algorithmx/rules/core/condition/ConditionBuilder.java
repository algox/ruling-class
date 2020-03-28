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
import org.algorithmx.rules.model.ConditionDefinition;
import org.algorithmx.rules.model.ParameterDefinition;

import java.lang.reflect.Type;

/**
 * Builder class used to create Conditions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public final class ConditionBuilder {

    private final FunctionalCondition condition;
    private final ConditionDefinition definition;

    private ConditionBuilder(FunctionalCondition condition) {
        super();
        this.condition = condition;
        this.definition = condition.getConditionDefinition();
    }

    /**
     * Creates a new condition builder given a ConditionConsumer. This is useful when using your own ConditionConsumer
     * definition or if you want to cast to an existing one.
     *
     * @param condition desired condition.
     * @return new ConditionBuilder based on the given consumer.
     */
    public static ConditionBuilder with(FunctionalCondition condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with no arguments.
     *
     * @param condition desired condition.
     * @return new ConditionBuilder with no arguments.
     */
    public static ConditionBuilder withNoArgs(NoArgCondition condition) {
        return new ConditionBuilder(condition);
    }

    public static ConditionBuilder alwaysTrue() {
        return ConditionBuilder.withNoArgs(() -> true);
    }

    public static ConditionBuilder alwaysFalse() {
        return ConditionBuilder.withNoArgs(() -> false);
    }

    /**
     * Creates a new condition builder with one argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @return new ConditionBuilder with one arguments.
     */
    public static <A> ConditionBuilder with1Arg(UnaryCondition<A> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with two argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new ConditionBuilder with two arguments.
     */
    public static <A, B> ConditionBuilder with2Args(BiCondition<A, B> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with three argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new ConditionBuilder with three arguments.
     */
    public static <A, B, C> ConditionBuilder with3Args(TriCondition<A, B, C> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with four argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new ConditionBuilder with four arguments.
     */
    public static <A, B, C, D> ConditionBuilder with4Args(QuadCondition<A, B, C, D> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with five argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new ConditionBuilder with five arguments.
     */
    public static <A, B, C, D, E> ConditionBuilder with5Args(QuinCondition<A, B, C, D, E> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with six argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new ConditionBuilder with six arguments.
     */
    public static <A, B, C, D, E, F> ConditionBuilder with6Args(SexCondition<A, B, C, D, E, F> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with seven argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new ConditionBuilder with seven arguments.
     */
    public static <A, B, C, D, E, F, G> ConditionBuilder with7Args(SeptCondition<A, B, C, D, E, F, G> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with eight argument.
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
     * @return new ConditionBuilder with eight arguments.
     */
    public static <A, B, C, D, E, F, G, H> ConditionBuilder with8Args(OctCondition<A, B, C, D, E, F, G, H> condition) {
        return new ConditionBuilder(condition);
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
    public static <A, B, C, D, E, F, G, H, I> ConditionBuilder with9Args(NovCondition<A, B, C, D, E, F, G, H, I> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with ten argument.
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
     * @return new ConditionBuilder with ten arguments.
     */
    public static <A, B, C, D, E, F, G, H, I, J> ConditionBuilder with10Args(DecCondition<A, B, C, D, E, F, G, H, I, J> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Change the parameter type, useful for Conditions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param index parameter index.
     * @param type desired type.
     * @return ConditionBuilder for fluency.
     */
    public ConditionBuilder parameterType(int index, Type type) {

        if (definition.getMethodDefinition().getParameterDefinitions().length == 0) {
            throw new UnrulyException("There are no args found");
        }

        if (index < 0 || index > definition.getMethodDefinition().getParameterDefinitions().length) {
            throw new UnrulyException("Invalid parameter index [" + index + "] it must be between [0, "
                    + definition.getMethodDefinition().getParameterDefinitions().length + "]");
        }

        this.definition.getMethodDefinition().getParameterDefinition(index).setType(type);
        return this;
    }

    /**
     * Change the parameter type, useful for Conditions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param name parameter name.
     * @param type desired type.
     * @return ConditionBuilder for fluency.
     */
    public ConditionBuilder parameterType(String name, Type type) {
        ParameterDefinition definition = this.definition.getMethodDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter [" + name + "] found");
        }

        definition.setType(type);
        return this;
    }

    /**
     * Provide a description for the Condition.
     *
     * @param description description of the Condition.
     * @return ConditionBuilder for fluency.
     */
    public ConditionBuilder description(String description) {
        this.definition.setDescription(description);
        return this;
    }

    /**
     * Builds the Condition based on the set properties.
     *
     * @return a new Condition.
     */
    public Condition build() {
        return new DelegatingCondition(condition, definition);
    }

    public ConditionDefinition getDefinition() {
        return definition;
    }
}
