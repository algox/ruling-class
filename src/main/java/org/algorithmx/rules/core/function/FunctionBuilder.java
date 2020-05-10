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

import org.algorithmx.rules.annotation.Function;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

/**
 * Builder class for Functions.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class FunctionBuilder<T> extends ExecutableBuilder {

    protected FunctionBuilder(Object function, MethodDefinition definition) {
        super(function, definition);
    }

    public static FunctionBuilder with(Object function, Class<? extends Annotation> annotation) {
        MethodInfo methodInfo = load(function, annotation);
        return new FunctionBuilder(methodInfo.getFunction(), methodInfo.getDefinition());
    }

    /*public static FunctionBuilder with(Class<?> functionClass, Class<? extends Annotation> annotation) {
        Method functionMethod = findFunctionMethod(functionClass, annotation);

        if (functionMethod == null) {
            throw new UnrulyException("Class [" + functionClass + "] does not implement any function methods. " +
                    "Add @Function to a method and try again.");
        }

        return new FunctionBuilder(with(ObjectFactory.create().create(functionClass), functionMethod));
    }*/

    /**
     * Builds the Action based on the set properties.
     *
     * @return a new Action.
     */
    public org.algorithmx.rules.core.function.Function<T> build() {
        return new DefaultFunction(getFunction(), getDefinition());
    }

    /**
     * Creates a new action builder with no arguments.
     *
     * @param function desired action.
     * @return new ActionBuilder with no arguments.
     */
    public static <T> FunctionBuilder<T> with(NoArgFunction<T> function) {
        return with(function, Function.class);
    }

    /**
     * Creates a new action builder with one argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @return new ActionBuilder with one arguments.
     */
    public static <T, A> FunctionBuilder with(UnaryFunction<T, A> function) {
        return with(function, Function.class);
    }

    /**
     * Creates a new action builder with two argument.
     *
     * @param Function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new ActionBuilder with two arguments.
     */
    public static <T, A, B> FunctionBuilder with(BiFunction<T, A, B> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with three argument.
     *
     * @param Function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new ActionBuilder with three arguments.
     */
    public static <T, A, B, C> FunctionBuilder with(TriFunction<T, A, B, C> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with four argument.
     *
     * @param Function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new ActionBuilder with four arguments.
     */
    public static <T, A, B, C, D> FunctionBuilder with(QuadFunction<T, A, B, C, D> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with five argument.
     *
     * @param Function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new ActionBuilder with five arguments.
     */
    public static <T, A, B, C, D, E> FunctionBuilder with(QuinFunction<T, A, B, C, D, E> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with six argument.
     *
     * @param Function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new ActionBuilder with six arguments.
     */
    public static <T, A, B, C, D, E, F> FunctionBuilder with(SexFunction<T, A, B, C, D, E, F> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with seven argument.
     *
     * @param Function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new ActionBuilder with seven arguments.
     */
    public static <T, A, B, C, D, E, F, G> FunctionBuilder with(SeptFunction<T, A, B, C, D, E, F, G> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with eight argument.
     *
     * @param Function desired action.
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
    public static <T, A, B, C, D, E, F, G, H> FunctionBuilder with(OctFunction<T, A, B, C, D, E, F, G, H> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action with nine argument.
     *
     * @param Function action action.
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
    public static <T, A, B, C, D, E, F, G, H, I> FunctionBuilder with(NovFunction<T, A, B, C, D, E, F, G, H, I> Function) {
        return with(Function, Function.class);
    }

    /**
     * Creates a new action builder with ten argument.
     *
     * @param Function desired action.
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
    public static <T, A, B, C, D, E, F, G, H, I, J> FunctionBuilder with(DecFunction<T, A, B, C, D, E, F, G, H, I, J> Function) {
        return with(Function, Function.class);
    }

    /**
     * Change the parameter type, useful for Actions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param index parameter index.
     * @param type desired type.
     * @return ActionBuilder for fluency.
     */
    public FunctionBuilder<T> parameterType(int index, Type type) {
        getDefinition().getParameterDefinition(index).setType(type);
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
    public FunctionBuilder<T> parameterType(String name, Type type) {
        ParameterDefinition definition = getDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter [" + name + "] found");
        }

        definition.setType(type);
        return this;
    }

    /**
     * Change the parameter name, useful for Actions where we are unable to retrieve the parameter name.
     *
     * @param index parameter index.
     * @param name new name.
     * @return ActionBuilder for fluency.
     */
    public FunctionBuilder<T> parameterName(int index, String name) {
        getDefinition().getParameterDefinition(index).setName(name);
        getDefinition().createParameterNameIndex();
        return this;
    }

    /**
     * Change the parameter type, useful for Actions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param index parameter index.
     * @param description desired description.
     * @return ActionBuilder for fluency.
     */
    public FunctionBuilder<T> parameterDescription(int index, String description) {
        getDefinition().getParameterDefinition(index).setDescription(description);
        return this;
    }

    /**
     * Change the parameter type, useful for Actions with generic types (as Java Compiler does not store generic
     * type for lambdas).
     *
     * @param name parameter name.
     * @param description desired description.
     * @return ActionBuilder for fluency.
     */
    public FunctionBuilder<T> parameterDescription(String name, String description) {
        ParameterDefinition definition = getDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter [" + name + "] found");
        }

        definition.setDescription(description);
        return this;
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

}
