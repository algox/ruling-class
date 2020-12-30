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

import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.core.model.ParameterDefinitionEditor;
import org.algorithmx.rules.core.context.RuleContext;
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
    public Function<T> build() {
        getDefinition().validate();
        return new DefaultFunction(getTarget(), getDefinition());
    }

    public static <T> Function<T> script(String script) {
        FunctionBuilder<T> builder = with((@Match(using = MatchByTypeMatchingStrategy.class) RuleContext ctx) -> {
            Object value = ctx.getScriptProcessor().evaluate(script, ctx.getBindings());
            T result;

            try {
                result = (T) value;
            } catch (ClassCastException e) {
                throw new UnrulyException("Function returned an invalid type. " +
                        "Actual [" + value + "]. Script [" + script + "]");
            }

            return result;
        });
        return builder.build();
    }

    /**
     * Creates a new function builder with no arguments.
     *
     * @param function desired action.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with no arguments.
     */
    public static <T> FunctionBuilder<T> with(NoArgFunction<T> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with no arguments.
     *
     * @param function desired action.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with no arguments.
     */
    public static <T> Function<T> create(NoArgFunction<T> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with one argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with one arguments.
     */
    public static <T, A> FunctionBuilder<T> with(UnaryFunction<T, A> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with one argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <T> generic return type of the function.
     * @return new function with one arguments.
     */
    public static <T, A> Function<T> create(UnaryFunction<T, A> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with two argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with two arguments.
     */
    public static <T, A, B> FunctionBuilder<T> with(BiFunction<T, A, B> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with two argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <T> generic return type of the function.
     * @return new function with two arguments.
     */
    public static <T, A, B> Function<T> create(BiFunction<T, A, B> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with three argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with three arguments.
     */
    public static <T, A, B, C> FunctionBuilder<T> with(TriFunction<T, A, B, C> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with three argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <T> generic return type of the function.
     * @return new function with three arguments.
     */
    public static <T, A, B, C> Function create(TriFunction<T, A, B, C> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with four argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with four arguments.
     */
    public static <T, A, B, C, D> FunctionBuilder<T> with(QuadFunction<T, A, B, C, D> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with four argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <T> generic return type of the function.
     * @return new function with four arguments.
     */
    public static <T, A, B, C, D> Function<T> create(QuadFunction<T, A, B, C, D> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with five argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with five arguments.
     */
    public static <T, A, B, C, D, E> FunctionBuilder<T> with(QuinFunction<T, A, B, C, D, E> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with five argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <T> generic return type of the function.
     * @return new Function with five arguments.
     */
    public static <T, A, B, C, D, E> Function<T> create(QuinFunction<T, A, B, C, D, E> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with six argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <T> generic return type of the function.
     * @return new FunctionBuilder with six arguments.
     */
    public static <T, A, B, C, D, E, F> FunctionBuilder<T> with(SexFunction<T, A, B, C, D, E, F> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with six argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <T> generic return type of the function.
     * @return new Function with six arguments.
     */
    public static <T, A, B, C, D, E, F> Function<T> create(SexFunction<T, A, B, C, D, E, F> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with seven argument.
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
     * @return new FunctionBuilder with seven arguments.
     */
    public static <T, A, B, C, D, E, F, G> FunctionBuilder<T> with(SeptFunction<T, A, B, C, D, E, F, G> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with seven argument.
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
     * @return new Function with seven arguments.
     */
    public static <T, A, B, C, D, E, F, G> Function<T> create(SeptFunction<T, A, B, C, D, E, F, G> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with eight argument.
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
     * @return new FunctionBuilder with eight arguments.
     */
    public static <T, A, B, C, D, E, F, G, H> FunctionBuilder<T> with(OctFunction<T, A, B, C, D, E, F, G, H> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with eight argument.
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
     * @return new Function with eight arguments.
     */
    public static <T, A, B, C, D, E, F, G, H> Function<T> create(OctFunction<T, A, B, C, D, E, F, G, H> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with nine argument.
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
     * @return new FunctionBuilder with nine arguments.
     */
    public static <T, A, B, C, D, E, F, G, H, I> FunctionBuilder<T> with(NovFunction<T, A, B, C, D, E, F, G, H, I> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function with nine argument.
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
     * @return new Function with nine arguments.
     */
    public static <T, A, B, C, D, E, F, G, H, I> Function<T> create(NovFunction<T, A, B, C, D, E, F, G, H, I> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
    }

    /**
     * Creates a new function builder with ten argument.
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
     * @return new FunctionBuilder with ten arguments.
     */
    public static <T, A, B, C, D, E, F, G, H, I, J> FunctionBuilder<T> with(DecFunction<T, A, B, C, D, E, F, G, H, I, J> function) {
        return withFunction(function);
    }

    /**
     * Creates a new function builder with ten argument.
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
     * @return new FunctionBuilder with ten arguments.
     */
    public static <T, A, B, C, D, E, F, G, H, I, J> Function<T> create(DecFunction<T, A, B, C, D, E, F, G, H, I, J> function) {
        FunctionBuilder<T> builder = withFunction(function);
        return builder.build();
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
