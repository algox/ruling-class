
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
package org.algorithmx.rules.build;

import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.ConditionConsumer;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ConditionDefinition;
import org.algorithmx.rules.model.ParameterDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ConditionUtils;

import java.lang.reflect.Type;

/**
 * Builder class used to create Conditions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ConditionBuilder {

    private final ConditionConsumer condition;
    private final ConditionDefinition definition;

    private ConditionBuilder(ConditionConsumer condition) {
        super();
        Assert.notNull(condition, "condition cannot be null");
        this.condition = condition;
        this.definition = ConditionUtils.load(condition, null);
    }

    /**
     * Creates a new condition builder with no arguments.
     *
     * @param condition desired condition.
     * @return new Condition Builder with no arguments.
     */
    public static ConditionBuilder withNoArgs(ConditionConsumer.Condition0 condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with one argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @return new Condition Builder with one arguments.
     */
    public static <A> ConditionBuilder with1Arg(ConditionConsumer.Condition1<A> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with two argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new Condition Builder with two arguments.
     */
    public static <A, B> ConditionBuilder with2Args(ConditionConsumer.Condition2<A, B> condition) {
        return new ConditionBuilder(condition);
    }

    /**
     * Creates a new condition builder with three argument.
     *
     * @param condition desired condition.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new Condition Builder with three arguments.
     */
    public static <A, B, C> ConditionBuilder with3Args(ConditionConsumer.Condition3<A, B, C> condition) {
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
     * @return new Condition Builder with four arguments.
     */
    public static <A, B, C, D> ConditionBuilder with4Args(ConditionConsumer.Condition4<A, B, C, D> condition) {
        return new ConditionBuilder(condition);
    }

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

    public ConditionBuilder parameterType(String name, Type type) {
        ParameterDefinition definition = this.definition.getMethodDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter [" + name + "] found");
        }

        definition.setType(type);
        return this;
    }

    public ConditionBuilder description(String description) {
        this.definition.setDescription(description);
        return this;
    }

    public Condition build() {
        return ConditionUtils.create(definition, null);
    }
}
