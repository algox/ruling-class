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

import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.function.ExecutableBuilder;
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
 * Builder class for Actions.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public final class ActionBuilder extends ExecutableBuilder {

    private static final Predicate<Method> FILTER =
            m -> ReflectionUtils.isAnnotated(m, org.algorithmx.rules.annotation.Action.class)
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

    public static Action script(String script) {
        return with((@Match(using = MatchByTypeMatchingStrategy.class) RuleContext ctx) -> {
            ctx.getScriptProcessor().evaluate(script, ctx.getBindings());
        }).build();
    }

    public static ActionBuilder with(Object target, MethodDefinition definition) {
        return new ActionBuilder(target, definition);
    }

    private static ActionBuilder withAction(Object target) {
        Method[] candidates = ReflectionUtils.getMethods(target.getClass(), FILTER);

        if (candidates == null || candidates.length == 0) {
            throw new UnrulyException("Action method not found on class [" + target.getClass() + "]");
        }

        // Too many Actions declared
        if (candidates.length > 1) {
            throw new UnrulyException("Too many action methods found on class [" + target.getClass() + "]. Candidates ["
                    + Arrays.toString(candidates) + "]");
        }

        MethodInfo methodInfo = load(target, candidates[0]);

        if (!void.class.equals(methodInfo.getDefinition().getReturnType())) {
            throw new UnrulyException("Actions must return a void [" + methodInfo.getDefinition().getMethod() + "]");
        }

        return new ActionBuilder(methodInfo.getTarget(), methodInfo.getDefinition());
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
    public static Action create(NoArgAction action) {
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
    public static <A> Action create(UnaryAction<A> action) {
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
    public static <A, B> Action create(BiAction<A, B> action) {
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
    public static <A, B, C> Action create(TriAction<A, B, C> action) {
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
    public static <A, B, C, D> Action create(QuadAction<A, B, C, D> action) {
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
    public static <A, B, C, D, E> Action create(QuinAction<A, B, C, D, E> action) {
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
    public static <A, B, C, D, E, F> Action create(SexAction<A, B, C, D, E, F> action) {
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
    public static <A, B, C, D, E, F, G> Action create(SeptAction<A, B, C, D, E, F, G> action) {
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
    public static <A, B, C, D, E, F, G, H> Action create(OctAction<A, B, C, D, E, F, G, H> action) {
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
    public static <A, B, C, D, E, F, G, H, I> Action create(NovAction<A, B, C, D, E, F, G, H, I> action) {
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
    public static <A, B, C, D, E, F, G, H, I, J> Action create(DecAction<A, B, C, D, E, F, G, H, I, J> action) {
        return withAction(action).build();
    }

    /**
     * Provide a name for the Action.
     *
     * @param name name of the Action.
     * @return ActionBuilder for fluency.
     */
    public ActionBuilder name(String name) {
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

}
