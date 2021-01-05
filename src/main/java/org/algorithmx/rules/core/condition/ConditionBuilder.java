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

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.function.BiFunction;
import org.algorithmx.rules.core.function.DecFunction;
import org.algorithmx.rules.core.function.ExecutableBuilder;
import org.algorithmx.rules.core.function.NoArgFunction;
import org.algorithmx.rules.core.function.NovFunction;
import org.algorithmx.rules.core.function.OctFunction;
import org.algorithmx.rules.core.function.QuadFunction;
import org.algorithmx.rules.core.function.QuinFunction;
import org.algorithmx.rules.core.function.SeptFunction;
import org.algorithmx.rules.core.function.SexFunction;
import org.algorithmx.rules.core.function.TriFunction;
import org.algorithmx.rules.core.function.UnaryFunction;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.core.model.ParameterDefinitionEditor;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.reflect.ObjectFactory;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Builder class used to defaultObjectFactory Conditions.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ConditionBuilder extends ExecutableBuilder {

    private static final Predicate<Method> FILTER = m -> ReflectionUtils
            .isAnnotated(m, org.algorithmx.rules.annotation.Function.class)
            && Modifier.isPublic(m.getModifiers()) && !m.isBridge();

    protected ConditionBuilder(Object target, MethodDefinition definition) {
        super(target, definition);
    }

    private static ConditionBuilder with(Object target, MethodDefinition definition) {
        return new ConditionBuilder(target, definition);
    }

    public static Condition[] build(Class<?> clazz) {
        return build(clazz, ObjectFactory.create());
    }

    public static Condition[] build(Class<?> clazz, ObjectFactory factory) {
        return build(factory.create(clazz));
    }

    public static Condition[] build(Object target) {
        return build(target, org.algorithmx.rules.annotation.Condition.class, null);
    }

    public static Condition[] build(Object target, Class<? extends Annotation> annotationClass, Integer max) {
        Assert.notNull(annotationClass, "annotationClass cannot be null.");
        Class<?> clazz = target.getClass();
        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(clazz, annotationClass);

        if (max != null && candidates.length > max) {
            // Too many matches
            throw new UnrulyException(clazz.getSimpleName() + " class has too many "
                    + annotationClass.getSimpleName() + " condition methods. " + "There can be at most " + max
                    + " methods (Annotated with @" + annotationClass.getSimpleName()
                    + "). Currently there are [" + candidates.length
                    + "] candidates [" + Arrays.toString(candidates) + "]");
        }

        for (Method candidate : candidates) {
            if (!(candidate.getReturnType().equals(boolean.class) || candidate.getReturnType().equals(Boolean.class))) {
                throw new UnrulyException("Condition" + annotationClass.getSimpleName() + " must return a boolean. "
                        + clazz.getSimpleName() + " method " + candidate
                        + "] returns a [" + candidate.getReturnType() + "]");
            }

        }

        Condition[] result = new Condition[candidates.length];

        for (int i = 0; i < candidates.length; i++) {
            result[i] = with(target, MethodDefinition.load(candidates[i])).build();
        }

        return result;
    }

    private static ConditionBuilder withCondition(Object target) {
        Method[] candidates = ReflectionUtils.getMethods(target.getClass(), FILTER);

        if (candidates == null || candidates.length == 0) {
            throw new UnrulyException("Condition method not found on class [" + target.getClass() + "]");
        }

        // Too many Actions declared
        if (candidates.length > 1) {
            throw new UnrulyException("Too many condition methods found on class [" + target.getClass() + "]. Candidates ["
                    + Arrays.toString(candidates) + "]");
        }

        Method implementationMethod = ReflectionUtils.getImplementationMethod(target.getClass(), candidates[0]);
        MethodInfo methodInfo = load(target, implementationMethod);

        if (!boolean.class.equals(methodInfo.getDefinition().getReturnType()) &&
                !Boolean.class.equals(methodInfo.getDefinition().getReturnType())) {
            throw new UnrulyException("Conditions must return a boolean [" + methodInfo.getDefinition().getMethod() + "]");
        }

        return new ConditionBuilder(methodInfo.getTarget(), methodInfo.getDefinition());
    }

    /**
     * Builds the Action based on the set properties.
     *
     * @return a new Action.
     */
    public Condition build() {
        getDefinition().validate();
        return new DefaultCondition(getTarget(), getDefinition());
    }

    public static Condition TRUE() {
        return with(() -> true).build();
    }

    public static Condition FALSE() {
        return with(() -> false).build();
    }

    public static Condition build(String script) {
        return with((RuleContext context) -> {
            Object result = context.getScriptProcessor().evaluate(script, context.getBindings());

            if (result == null) throw new UnrulyException("Script Condition excepts a boolean return type. " +
                    "Actual [null]. Script [" + script + "]");
            if (!(result instanceof Boolean)) throw new UnrulyException("Condition excepts a boolean return type. " +
                    "Actual [" + result + "]. Script [" + script + "]");

            return (Boolean) result;
        }).build();
    }

    /**
     * Creates a new action builder with no arguments.
     *
     * @param function desired action.
     * @return new ActionBuilder with no arguments.
     */
    public static ConditionBuilder with(NoArgFunction<Boolean> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with no arguments.
     *
     * @param function desired action.
     * @return new condition with no arguments.
     */
    public static Condition build(NoArgFunction<Boolean> function) {
        return withCondition(function).build();
    }

    /**
     * Creates a new action builder with one argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @return new ActionBuilder with one arguments.
     */
    public static <A> ConditionBuilder with(UnaryFunction<Boolean, A> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with one argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @return new Condition representing the passed in function.
     */
    public static <A> Condition build(UnaryFunction<Boolean, A> function) {
        return withCondition(function).build();
    }

    /**
     * Creates a new action builder with two argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new ActionBuilder with two arguments.
     */
    public static <A, B> ConditionBuilder with(BiFunction<Boolean, A, B> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with two argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new condition with two arguments.
     */
    public static <A, B> Condition build(BiFunction<Boolean, A, B> function) {
        return withCondition(function).build();
    }

    /**
     * Creates a new action builder with three argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new ActionBuilder with three arguments.
     */
    public static <A, B, C> ConditionBuilder with(TriFunction<Boolean, A, B, C> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with three argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new condition with three arguments.
     */
    public static <A, B, C> Condition build(TriFunction<Boolean, A, B, C> function) {
        return withCondition(function).build();
    }

    /**
     * Creates a new action builder with four argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new ActionBuilder with four arguments.
     */
    public static <A, B, C, D> ConditionBuilder with(QuadFunction<Boolean, A, B, C, D> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition builder with four argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new condition with four arguments.
     */
    public static <A, B, C, D> Condition build(QuadFunction<Boolean, A, B, C, D> function) {
        return withCondition(function).build();
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
     * @return new ActionBuilder with five arguments.
     */
    public static <A, B, C, D, E> ConditionBuilder with(QuinFunction<Boolean, A, B, C, D, E> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition builder with five argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new condition with five arguments.
     */
    public static <A, B, C, D, E> Condition build(QuinFunction<Boolean, A, B, C, D, E> function) {
        return withCondition(function).build();
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
     * @return new ActionBuilder with six arguments.
     */
    public static <A, B, C, D, E, F> ConditionBuilder with(SexFunction<Boolean, A, B, C, D, E, F> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with six argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new condition with six arguments.
     */
    public static <A, B, C, D, E, F> Condition build(SexFunction<Boolean, A, B, C, D, E, F> function) {
        return withCondition(function).build();
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
     * @return new ActionBuilder with seven arguments.
     */
    public static <A, B, C, D, E, F, G> ConditionBuilder with(SeptFunction<Boolean, A, B, C, D, E, F, G> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with seven argument.
     *
     * @param function desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new condition with seven arguments.
     */
    public static <A, B, C, D, E, F, G> Condition build(SeptFunction<Boolean, A, B, C, D, E, F, G> function) {
        return withCondition(function).build();
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
     * @return new ActionBuilder with eight arguments.
     */
    public static <A, B, C, D, E, F, G, H> ConditionBuilder with(OctFunction<Boolean, A, B, C, D, E, F, G, H> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with eight argument.
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
     * @return new condition with eight arguments.
     */
    public static <A, B, C, D, E, F, G, H> Condition build(OctFunction<Boolean, A, B, C, D, E, F, G, H> function) {
        return withCondition(function).build();
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
     * @return new ActionBuilder with nine arguments.
     */
    public static <A, B, C, D, E, F, G, H, I> ConditionBuilder with(NovFunction<Boolean, A, B, C, D, E, F, G, H, I> function) {
        return withCondition(function);
    }

    /**
     * Creates a condition with nine argument.
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
     * @return new condition with nine arguments.
     */
    public static <A, B, C, D, E, F, G, H, I> Condition build(NovFunction<Boolean, A, B, C, D, E, F, G, H, I> function) {
        return withCondition(function).build();
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
     * @return new ActionBuilder with ten arguments.
     */
    public static <A, B, C, D, E, F, G, H, I, J> ConditionBuilder with(DecFunction<Boolean, A, B, C, D, E, F, G, H, I, J> function) {
        return withCondition(function);
    }

    /**
     * Creates a new condition with ten argument.
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
     * @return new condition with ten arguments.
     */
    public static <A, B, C, D, E, F, G, H, I, J> Condition build(DecFunction<Boolean, A, B, C, D, E, F, G, H, I, J> function) {
        return withCondition(function).build();
    }

    /**
     * Provide a name for the Action.
     *
     * @param name name of the Action.
     * @return ActionBuilder for fluency.
     */
    public ConditionBuilder name(String name) {
        getDefinition().setName(name);
        return this;
    }

    /**
     * Provide a description for the Action.
     *
     * @param description description of the Action.
     * @return ActionBuilder for fluency.
     */
    public ConditionBuilder description(String description) {
        getDefinition().setDescription(description);
        return this;
    }

    public ParameterDefinitionEditor<ConditionBuilder> param(int index) {
        return new ParameterDefinitionEditor(getDefinition().getParameterDefinition(index), this);
    }

    public ParameterDefinitionEditor<ConditionBuilder> param(String name) {
        ParameterDefinition definition = getDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter found [" + name + "] in method [" + getDefinition().getMethod() + "]");
        }

        return new ParameterDefinitionEditor(definition, this);
    }

}
