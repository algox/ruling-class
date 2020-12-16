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
package org.algorithmx.rules.core.function;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.core.model.ParameterDefinitionEditor;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Builder class for Functions.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class FunctionBuilder<T> extends ExecutableBuilder {

    private static final Predicate<Method> FILTER = m -> ReflectionUtils
            .isAnnotated(m, org.algorithmx.rules.annotation.Function.class)
            && Modifier.isPublic(m.getModifiers()) && !m.isBridge();

    protected FunctionBuilder(Object function, MethodDefinition definition) {
        super(function, definition);
    }

    private static <T> FunctionBuilder<T> withFunction(Object target) {
        Method[] candidates = ReflectionUtils.getMethods(target.getClass(), FILTER);

        if (candidates == null || candidates.length == 0) {
            throw new UnrulyException("Function method not found on class [" + target.getClass() + "]");
        }

        // Too many Actions declared
        if (candidates.length > 1) {
            throw new UnrulyException("Too many function methods found on class [" + target.getClass() + "]. Candidates ["
                    + Arrays.toString(candidates) + "]");
        }

        Method implementationMethod = ReflectionUtils.getImplementationMethod(target.getClass(), candidates[0]);
        MethodInfo methodInfo = load(target, implementationMethod);
        return (FunctionBuilder<T>) new FunctionBuilder(methodInfo.getTarget(), methodInfo.getDefinition());
    }


    /**
     * Builds the Action based on the set properties.
     *
     * @return a new Action.
     */
    public org.algorithmx.rules.core.function.Function<T> build() {
        getDefinition().validate();
        return new DefaultFunction(getTarget(), getDefinition());
    }

    /**
     * Creates a new action builder with no arguments.
     *
     * @param function desired action.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with no arguments.
     */
    public static <T> FunctionBuilder<T> with(NoArgFunction<T> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with one argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with one arguments.
     */
    public static <T, A> FunctionBuilder<T> with(UnaryFunction<T, A> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with two argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with two arguments.
     */
    public static <T, A, B> FunctionBuilder<T> with(BiFunction<T, A, B> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with three argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with three arguments.
     */
    public static <T, A, B, C> FunctionBuilder with(TriFunction<T, A, B, C> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with four argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with four arguments.
     */
    public static <T, A, B, C, D> FunctionBuilder with(QuadFunction<T, A, B, C, D> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with five argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with five arguments.
     */
    public static <T, A, B, C, D, E> FunctionBuilder with(QuinFunction<T, A, B, C, D, E> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with six argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with six arguments.
     */
    public static <T, A, B, C, D, E, F> FunctionBuilder with(SexFunction<T, A, B, C, D, E, F> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with seven argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with seven arguments.
     */
    public static <T, A, B, C, D, E, F, G> FunctionBuilder with(SeptFunction<T, A, B, C, D, E, F, G> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with eight argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with eight arguments.
     */
    public static <T, A, B, C, D, E, F, G, H> FunctionBuilder with(OctFunction<T, A, B, C, D, E, F, G, H> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action with nine argument.
     *
     * @param function action action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @param <H> generic type of the eighth parameter.
     * @param <I> generic type of the ninth parameter.
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with nine arguments.
     */
    public static <T, A, B, C, D, E, F, G, H, I> FunctionBuilder with(NovFunction<T, A, B, C, D, E, F, G, H, I> function) {
        return withFunction(function);
    }

    /**
     * Creates a new action builder with ten argument.
     *
     * @param function desired action.
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
     * @param <T> generic return type of the function.
     * @return new ActionBuilder with ten arguments.
     */
    public static <T, A, B, C, D, E, F, G, H, I, J> FunctionBuilder with(DecFunction<T, A, B, C, D, E, F, G, H, I, J> function) {
        return withFunction(function);
    }

    /**
     * Provide a name for the Action.
     *
     * @param name name of the Action.
     * @return ActionBuilder for fluency.
     */
    public FunctionBuilder<T> name(String name) {
        getDefinition().setName(name);
        return this;
    }

    /**
     * Provide a description for the Action.
     *
     * @param description description of the Action.
     * @return ActionBuilder for fluency.
     */
    public FunctionBuilder<T> description(String description) {
        getDefinition().setDescription(description);
        return this;
    }

    public ParameterDefinitionEditor<FunctionBuilder<T>> param(int index) {
        return new ParameterDefinitionEditor(getDefinition().getParameterDefinition(index), this);
    }

    public ParameterDefinitionEditor<FunctionBuilder<T>> param(String name) {
        ParameterDefinition definition = getDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter found [" + name + "] in method [" + getDefinition().getMethod() + "]");
        }

        return new ParameterDefinitionEditor(definition, this);
    }

}
