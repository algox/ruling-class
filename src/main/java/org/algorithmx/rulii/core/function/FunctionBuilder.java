/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.core.function;

import org.algorithmx.rulii.bind.ScopedBindings;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.core.model.ParameterDefinition;
import org.algorithmx.rulii.core.model.ParameterDefinitionEditor;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.script.ScriptLanguageManager;
import org.algorithmx.rulii.script.ScriptProcessor;
import org.algorithmx.rulii.util.SystemDefaultsHolder;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
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
            .isAnnotated(m, org.algorithmx.rulii.annotation.Function.class)
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

        MethodInfo methodInfo = load(target, candidates[0]);

        return (FunctionBuilder<T>) new FunctionBuilder(methodInfo.getTarget(), methodInfo.getDefinition());
    }

    public static Function[] build(Class<?> clazz) {
        Assert.notNull(clazz, "clazz cannot be null.");
        ObjectFactory objectFactory = SystemDefaultsHolder.getInstance().getDefaults().getObjectFactory();
        return build(clazz, objectFactory);
    }

    public static Function[] build(Class<?> clazz, ObjectFactory factory) {
        return build(factory.createFunction(clazz));
    }

    public static Function[] build(Object target) {
        return build(target, org.algorithmx.rulii.annotation.Function.class, null);
    }

    private static <T> FunctionBuilder<T> with(Object target, MethodDefinition definition) {
        return new FunctionBuilder(target, definition);
    }

    public static Function[] build(Object target, Class<? extends Annotation> annotationClass, Integer max) {
        Assert.notNull(annotationClass, "annotationClass cannot be null.");
        Class<?> clazz = target.getClass();
        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(clazz, annotationClass);

        if (max != null && candidates.length > max) {
            // Too many matches
            throw new UnrulyException(clazz.getSimpleName() + " class has too many "
                    + annotationClass.getSimpleName() + " function methods. " + "There can be at most " + max
                    + " methods (Annotated with @" + annotationClass.getSimpleName()
                    + "). Currently there are [" + candidates.length
                    + "] candidates [" + Arrays.toString(candidates) + "]");
        }

        Function[] result = new Function[candidates.length];

        for (int i = 0; i < candidates.length; i++) {
            result[i] = with(target, MethodDefinition.load(candidates[i])).build();
        }

        return result;
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

    public static <T> Function<T> build(String script, String scriptingLanguage) {
        FunctionBuilder<T> builder = with((RuleContext context) -> processScriptFunction(script, scriptingLanguage,
                context.getBindings()));
        return builder.build();
    }

    public static <T> Function<T> build(String script) {
        FunctionBuilder<T> builder = with((RuleContext context) -> processScriptFunction(script,
                context.getScriptingLanguage(), context.getBindings()));
        return builder.build();
    }

    private static <T> T processScriptFunction(String script, String language, ScopedBindings bindings) {
        ScriptProcessor scriptProcessor = ScriptLanguageManager.getScriptProcessor(language);

        if (scriptProcessor == null) {
            throw new UnrulyException("Unable to execute script function [" + script + "]. " +
                    "Could not find a scripting language [" + language + "]. Trying registering in ScriptLanguageManager and try again.");
        }

        Object value = scriptProcessor.evaluate(script, bindings);

        T result;

        try {
            result = (T) value;
        } catch (ClassCastException e) {
            throw new UnrulyException("Function returned an invalid type. " +
                    "Actual [" + value + "]. Script [" + script + "]");
        }

        return result;
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
    public static <T> Function<T> build(NoArgFunction<T> function) {
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
    public static <T, A> Function<T> build(UnaryFunction<T, A> function) {
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
    public static <T, A, B> Function<T> build(BiFunction<T, A, B> function) {
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
    public static <T, A, B, C> Function build(TriFunction<T, A, B, C> function) {
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
    public static <T, A, B, C, D> Function<T> build(QuadFunction<T, A, B, C, D> function) {
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
    public static <T, A, B, C, D, E> Function<T> build(QuinFunction<T, A, B, C, D, E> function) {
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
    public static <T, A, B, C, D, E, F> Function<T> build(SexFunction<T, A, B, C, D, E, F> function) {
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
    public static <T, A, B, C, D, E, F, G> Function<T> build(SeptFunction<T, A, B, C, D, E, F, G> function) {
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
    public static <T, A, B, C, D, E, F, G, H> Function<T> build(OctFunction<T, A, B, C, D, E, F, G, H> function) {
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
    public static <T, A, B, C, D, E, F, G, H, I> Function<T> build(NovFunction<T, A, B, C, D, E, F, G, H, I> function) {
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
    public static <T, A, B, C, D, E, F, G, H, I, J> Function<T> build(DecFunction<T, A, B, C, D, E, F, G, H, I, J> function) {
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
