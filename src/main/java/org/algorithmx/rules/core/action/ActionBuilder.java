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
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;
import org.algorithmx.rules.util.LambdaUtils;
import org.algorithmx.rules.util.reflect.ObjectFactory;

import java.lang.annotation.Annotation;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Builder class for Actions.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public final class ActionBuilder {

    private Object action;
    private MethodDefinition definition;

    private ActionBuilder(Object action, MethodDefinition definition) {
        super();
        Assert.notNull(definition, "actionMethod cannot be null.");
        this.action = action;
        this.definition = definition;
    }

    public static ActionBuilder with(Class<?> actionClass, Class<? extends Annotation> annotation) {
        Method actionableMethod = ActionUtils.findActionableMethod(actionClass, annotation);

        if (actionableMethod == null) {
            throw new UnrulyException("Class [" + actionClass + "] does not implement any actionable methods. " +
                    "Add @Action to a method and try again.");
        }

        return with(ObjectFactory.create().create(actionClass), actionableMethod);
    }

    public static ActionBuilder with(Object action, Class<? extends Annotation> annotation) {
        Assert.notNull(action, "action cannot be null.");

        Method actionableMethod = ActionUtils.findActionableMethod(action.getClass(), annotation);

        if (actionableMethod == null) {
            throw new UnrulyException("Class [" + action.getClass() + "] does not implement any actionable methods. " +
                    "Add @Action to a method and try again.");
        }

        SerializedLambda serializedLambda = LambdaUtils.getSafeSerializedLambda(action);

        if (serializedLambda != null) {
            return withLambda(action, actionableMethod, serializedLambda);
        }

        return with(action, actionableMethod);
    }

    public static ActionBuilder with(Object action, Method actionableMethod) {
        MethodDefinition definition = MethodDefinition.load(actionableMethod);
        return new ActionBuilder(action, definition);
    }

    private static ActionBuilder withLambda(Object action, Method actionableMethod, SerializedLambda serializedLambda) {
        Assert.notNull(actionableMethod, "actionableMethod cannot be null.");
        Assert.notNull(serializedLambda, "serializedLambda cannot be null.");
        MethodDefinition methodDefinition = null;

        try {
            Class<?> implementationClass = LambdaUtils.getImplementationClass(serializedLambda);
            Method implementationMethod = LambdaUtils.getImplementationMethod(serializedLambda, implementationClass);
            MethodDefinition implementationMethodDefinition = MethodDefinition.load(implementationMethod);

            ParameterDefinition[] parameterDefinitions = new ParameterDefinition[actionableMethod.getParameterCount()];
            int delta = implementationMethod.getParameterCount() - actionableMethod.getParameterCount();

            for (int i = delta; i < implementationMethod.getParameterCount(); i++) {
                int index = i - delta;
                parameterDefinitions[index] = implementationMethodDefinition.getParameterDefinition(i);
                parameterDefinitions[index].setIndex(index);
            }

            methodDefinition = new MethodDefinition(actionableMethod, implementationMethodDefinition.getOrder(),
                    implementationMethodDefinition.getDescription(), parameterDefinitions);

        } catch (Exception e) {
            // Log
        }

        if (methodDefinition == null) {
            methodDefinition = MethodDefinition.load(actionableMethod);
        }

        return new ActionBuilder(action, methodDefinition);
    }

    /**
     * Creates a new action builder with no arguments.
     *
     * @param action desired action.
     * @return new ActionBuilder with no arguments.
     */
    public static ActionBuilder with(NoArgAction action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * As the name suggestion, this create an Action that does nothing.
     *
     * @return do nothing action.
     */
    public static ActionBuilder emptyAction() {
        return ActionBuilder.with(() -> {});
    }

    /**
     * Creates a new action builder with one argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return new ActionBuilder with one arguments.
     */
    public static <A> ActionBuilder with(UnaryAction<A> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with two argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new ActionBuilder with two arguments.
     */
    public static <A, B> ActionBuilder with(BiAction<A, B> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with three argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new ActionBuilder with three arguments.
     */
    public static <A, B, C> ActionBuilder with(TriAction<A, B, C> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with four argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new ActionBuilder with four arguments.
     */
    public static <A, B, C, D> ActionBuilder with(QuadAction<A, B, C, D> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with five argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new ActionBuilder with five arguments.
     */
    public static <A, B, C, D, E> ActionBuilder with(QuinAction<A, B, C, D, E> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with six argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new ActionBuilder with six arguments.
     */
    public static <A, B, C, D, E, F> ActionBuilder with(SexAction<A, B, C, D, E, F> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with seven argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new ActionBuilder with seven arguments.
     */
    public static <A, B, C, D, E, F, G> ActionBuilder with(SeptAction<A, B, C, D, E, F, G> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with eight argument.
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
     * @return new ActionBuilder with eight arguments.
     */
    public static <A, B, C, D, E, F, G, H> ActionBuilder with(OctAction<A, B, C, D, E, F, G, H> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action with nine argument.
     *
     * @param action action action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @return new ActionBuilder with nine arguments.
     */
    public static <A, B, C, D, E, F, G, H, I> ActionBuilder with(NovAction<A, B, C, D, E, F, G, H, I> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Creates a new action builder with ten argument.
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
     * @return new ActionBuilder with ten arguments.
     */
    public static <A, B, C, D, E, F, G, H, I, J> ActionBuilder with(DecAction<A, B, C, D, E, F, G, H, I, J> action) {
        return with(action, org.algorithmx.rules.annotation.Action.class);
    }

    /**
     * Change the parameter type, useful for Actions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param index parameter index.
     * @param type desired type.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder parameterType(int index, Type type) {

        if (definition.getParameterDefinitions().length == 0) {
            throw new UnrulyException("There are no args found in the Action");
        }

        if (index < 0 || index >= definition.getParameterDefinitions().length) {
            throw new UnrulyException("Invalid parameter index [" + index + "] it must be between [0, "
                    + definition.getParameterDefinitions().length + "]");
        }

        this.definition.getParameterDefinition(index).setType(type);
        return this;
    }

    /**
     * Change the parameter type, useful for Actions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param name parameter name.
     * @param type desired type.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder parameterType(String name, Type type) {
        ParameterDefinition definition = this.definition.getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter [" + name + "] found");
        }

        definition.setType(type);
        return this;
    }

    /**
     * Provide a name for the Action.
     *
     * @param name name of the Action.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder name(String name) {
        this.definition.setName(name);
        return this;
    }

    /**
     * Provide a description for the Action.
     *
     * @param description description of the Action.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder description(String description) {
        this.definition.setDescription(description);
        return this;
    }

    /**
     * Builds the Action based on the set properties.
     *
     * @return a new Action.
     */
    public Action build() {
        return new DefaultAction(action, definition);
    }
}
