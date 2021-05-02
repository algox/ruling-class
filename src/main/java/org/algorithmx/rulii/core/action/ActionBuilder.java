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

package org.algorithmx.rulii.core.action;

import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.config.RuliiSystem;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.function.ExecutableBuilder;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.core.model.ParameterDefinition;
import org.algorithmx.rulii.core.model.ParameterDefinitionEditor;
import org.algorithmx.rulii.lib.spring.core.BridgeMethodResolver;
import org.algorithmx.rulii.lib.spring.core.annotation.AnnotationUtils;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.script.ScriptLanguageManager;
import org.algorithmx.rulii.script.ScriptProcessor;
import org.algorithmx.rulii.util.reflect.ObjectFactory;
import org.algorithmx.rulii.util.reflect.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.function.Predicate;

/**
 * Builder class for Actions.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public final class ActionBuilder extends ExecutableBuilder {

    private static final Predicate<Method> FILTER =
            m -> ReflectionUtils.isAnnotated(m, org.algorithmx.rulii.annotation.Action.class)
                    && Modifier.isPublic(m.getModifiers()) && !m.isBridge();


    protected ActionBuilder(Object target, MethodDefinition definition) {
        super(target, definition);
    }

    /**
     * Builds the Action based on the set properties.
     *
     * @return a new Action.
     */
    public Action build() {
        getDefinition().validate();
        return new DefaultAction(getTarget(), getDefinition());
    }

    public static Action build(String script, String scriptLanguage) {
        return with((@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context) -> {
            ScriptProcessor scriptProcessor = ScriptLanguageManager.getScriptProcessor(scriptLanguage);

            if (scriptProcessor == null) {
                throw new UnrulyException("Unable to execute script action [" + script + "]. " +
                        "Could not find scripting language [" + scriptLanguage
                        + "]. Trying registering in ScriptLanguageManager and try again.");
            }

            processScriptAction(script, scriptProcessor, context.getBindings());
        }).build();
    }

    public static Action build(String script) {
        return with((@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context)
                -> processScriptAction(script, context.getScriptProcessor(), context.getBindings())).build();
    }

    private static void processScriptAction(String script, ScriptProcessor scriptProcessor, Bindings bindings) {
        scriptProcessor.evaluate(script, bindings);
    }

    private static ActionBuilder with(Object target, MethodDefinition definition) {
        return new ActionBuilder(target, definition);
    }

    public static Action[] build(Class<?> clazz) {
        Assert.notNull(clazz, "clazz cannot be null.");
        ObjectFactory objectFactory = RuliiSystem.getInstance().getObjectFactory();
        return build(clazz, objectFactory);
    }

    public static Action[] build(Class<?> clazz, ObjectFactory factory) {
        return build(factory.createAction(clazz));
    }

    public static Action[] build(Object target) {
        return build(target, org.algorithmx.rulii.annotation.Action.class, null, null);
    }

    public static Action[] build(Object target, Class<? extends Annotation> annotationClass, Integer min, Integer max) {
        Assert.notNull(target, "target cannot be null.");
        Assert.notNull(annotationClass, "annotationClass cannot be null.");

        Class<?> clazz = target.getClass();
        Method[] candidates = ReflectionUtils.getMethodsWithAnnotation(clazz, annotationClass);

        if (min != null && candidates.length < min) {
            throw new UnrulyException("Not enough Action method(s) on class [" + target.getClass()
                    + "]. There must be at least " + min + " methods (Annotated with @" + annotationClass.getSimpleName()
                    + "). Currently there are [" + candidates.length
                    + "] candidates [" + Arrays.toString(candidates) + "]");
        }

        if (max != null && candidates.length > max) {
            // Too many matches
            throw new UnrulyException(clazz.getSimpleName() + " class has too many "
                    + annotationClass.getSimpleName() + " action methods. " + "There can be at most " + max
                    + " methods (Annotated with @" + annotationClass.getSimpleName()
                    + "). Currently there are [" + candidates.length
                    + "] candidates [" + Arrays.toString(candidates) + "]");
        }

        for (Method candidate : candidates) {
            if (!(candidate.getReturnType().equals(void.class))) {
                throw new UnrulyException("Action" + annotationClass.getSimpleName() + " must return a void. "
                        + clazz.getSimpleName() + " method " + candidate
                        + "] returns a [" + candidate.getReturnType() + "]");
            }

        }

        Action[] result = new Action[candidates.length];

        for (int i = 0; i < candidates.length; i++) {
            String name = extractName(candidates[0]);
            Method candidate = BridgeMethodResolver.findBridgedMethod(candidates[0]);
            ActionBuilder builder = with(target, MethodDefinition.load(candidate));
            if (name != null) builder.name(name);
            result[i] = builder.build();
        }

        return result;
    }

    public static ActionBuilder withAction(Object target) {
        Method[] candidates = ReflectionUtils.getMethods(target.getClass(), FILTER);

        if (candidates == null || candidates.length == 0) {
            throw new UnrulyException("Action method not found on class [" + target.getClass() + "]");
        }

        // Too many Actions declared
        if (candidates.length > 1) {
            throw new UnrulyException("Too many action methods found on class [" + target.getClass() + "]. Candidates ["
                    + Arrays.toString(candidates) + "]");
        }

        String name = extractName(candidates[0]);
        Method candidate = BridgeMethodResolver.findBridgedMethod(candidates[0]);
        MethodInfo methodInfo = load(target, candidate);

        if (!void.class.equals(methodInfo.getDefinition().getReturnType())) {
            throw new UnrulyException("Actions must return a void [" + methodInfo.getDefinition().getMethod() + "]");
        }

        ActionBuilder result = new ActionBuilder(methodInfo.getTarget(), methodInfo.getDefinition());

        if (name != null) result.name(name);

        return result;
    }

    /**
     * As the name suggestion, this create an Action that does nothing.
     *
     * @return do nothing action.
     */
    public static Action emptyAction() {
        return ActionBuilder.with(() -> {}).build();
    }

    /**
     * Creates a new action builder with no arguments.
     *
     * @param action desired action.
     * @return new ActionBuilder with no arguments.
     */
    public static ActionBuilder with(NoArgAction action) {
        return withAction(action);
    }

    /**
     * Creates a new action with no arguments.
     *
     * @param action desired action.
     * @return new action with no arguments.
     */
    public static Action build(NoArgAction action) {
        return withAction(action).build();
    }

    /**
     * Creates a new action builder with one argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return new ActionBuilder with one arguments.
     */
    public static <A> ActionBuilder with(UnaryAction<A> action) {
        return withAction(action);
    }

    /**
     * Creates a new action with one argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return new Action with one arguments.
     */
    public static <A> Action build(UnaryAction<A> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with two argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new Action with two arguments.
     */
    public static <A, B> Action build(BiAction<A, B> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with three argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @return new action with three arguments.
     */
    public static <A, B, C> Action build(TriAction<A, B, C> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with four argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @return new action with four arguments.
     */
    public static <A, B, C, D> Action build(QuadAction<A, B, C, D> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with five argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @return new action with five arguments.
     */
    public static <A, B, C, D, E> Action build(QuinAction<A, B, C, D, E> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with six argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @return new action with six arguments.
     */
    public static <A, B, C, D, E, F> Action build(SexAction<A, B, C, D, E, F> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with seven argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @param <C> generic type of the third parameter.
     * @param <D> generic type of the fourth parameter.
     * @param <E> generic type of the fifth parameter.
     * @param <F> generic type of the sixth parameter.
     * @param <G> generic type of the seventh parameter.
     * @return new action with seven arguments.
     */
    public static <A, B, C, D, E, F, G> Action build(SeptAction<A, B, C, D, E, F, G> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with eight argument.
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
     * @return new action with eight arguments.
     */
    public static <A, B, C, D, E, F, G, H> Action build(OctAction<A, B, C, D, E, F, G, H> action) {
        return withAction(action).build();
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
        return withAction(action);
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
     * @return new action with nine arguments.
     */
    public static <A, B, C, D, E, F, G, H, I> Action build(NovAction<A, B, C, D, E, F, G, H, I> action) {
        return withAction(action).build();
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
        return withAction(action);
    }

    /**
     * Creates a new action with ten argument.
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
     * @return new action with ten arguments.
     */
    public static <A, B, C, D, E, F, G, H, I, J> Action build(DecAction<A, B, C, D, E, F, G, H, I, J> action) {
        return withAction(action).build();
    }

    /**
     * Provide a name for the Action.
     *
     * @param name name of the Action.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder name(String name) {
        Assert.notNull(name, "name cannot be null.");
        getDefinition().setName(name);
        return this;
    }

    /**
     * Provide a description for the Action.
     *
     * @param description description of the Action.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder description(String description) {
        getDefinition().setDescription(description);
        return this;
    }

    public ParameterDefinitionEditor<ActionBuilder> param(int index) {
        return new ParameterDefinitionEditor(getDefinition().getParameterDefinition(index), this);
    }

    public ParameterDefinitionEditor<ActionBuilder> param(String name) {
        ParameterDefinition definition = getDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter found [" + name + "] in method [" + getDefinition().getMethod() + "]");
        }

        return new ParameterDefinitionEditor(definition, this);
    }

    private static String extractName(Method method) {
        org.algorithmx.rulii.annotation.Action action = AnnotationUtils.getAnnotation(method, org.algorithmx.rulii.annotation.Action.class);

        if (action == null) return null;

        return org.algorithmx.rulii.annotation.Action.NOT_APPLICABLE.equals(action.name()) ? null : action.name();
    }
}
