package org.algorithmx.rules.build;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.ActionConsumer;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.ParameterDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;

import java.lang.reflect.Type;

public final class ActionBuilder {

    private final ActionConsumer action;
    private final ActionDefinition definition;

    private ActionBuilder(ActionConsumer action, ActionDefinition definition) {
        super();
        Assert.notNull(action, "action cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        this.action = action;
        this.definition = definition;
    }

    /**
     * Creates a new action builder given a ActionConsumer. This is useful when using your own ActionConsumer
     * definition or if you want to cast to an existing one.
     *
     * @param consumer desired action.
     * @param definition action meta-information.
     * @return new ActionBuilder based on the given consumer.
     */
    public static ActionBuilder with(ActionConsumer consumer, ActionDefinition definition) {
        return new ActionBuilder(consumer, definition);
    }

    /**
     * Creates a new action builder with no arguments.
     *
     * @param action desired action.
     * @return new ActionBuilder with no arguments.
     */
    public static ActionBuilder withNoArgs(ActionConsumer.ActionConsumer0 action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
    }

    public static Action emptyAction() {
        return ActionBuilder.withNoArgs(() -> {}).build();
    }

    /**
     * Creates a new action builder with one argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @return new ActionBuilder with one arguments.
     */
    public static <A> ActionBuilder with1Arg(ActionConsumer.ActionConsumer1<A> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
    }

    /**
     * Creates a new action builder with two argument.
     *
     * @param action desired action.
     * @param <A> generic type of the first parameter.
     * @param <B> generic type of the second parameter.
     * @return new ActionBuilder with two arguments.
     */
    public static <A, B> ActionBuilder with2Args(ActionConsumer.ActionConsumer2<A, B> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C> ActionBuilder with3Args(ActionConsumer.ActionConsumer3<A, B, C> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D> ActionBuilder with4Args(ActionConsumer.ActionConsumer4<A, B, C, D> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D, E> ActionBuilder with5Args(ActionConsumer.ActionConsumer5<A, B, C, D, E> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D, E, F> ActionBuilder with6Args(ActionConsumer.ActionConsumer6<A, B, C, D, E, F> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D, E, F, G> ActionBuilder with7Args(ActionConsumer.ActionConsumer7<A, B, C, D, E, F, G> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D, E, F, G, H> ActionBuilder with8Args(
            ActionConsumer.ActionConsumer8<A, B, C, D, E, F, G, H> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D, E, F, G, H, I> ActionBuilder with9Args(
            ActionConsumer.ActionConsumer9<A, B, C, D, E, F, G, H, I> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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
    public static <A, B, C, D, E, F, G, H, I, J> ActionBuilder with10Args(
            ActionConsumer.ActionConsumer10<A, B, C, D, E, F, G, H, I, J> action) {
        return new ActionBuilder(action, ActionUtils.load(action, null));
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

        if (definition.getMethodDefinition().getParameterDefinitions().length == 0) {
            throw new UnrulyException("There are no args found in the Action");
        }

        if (index < 0 || index > definition.getMethodDefinition().getParameterDefinitions().length) {
            throw new UnrulyException("Invalid parameter index [" + index + "] it must be between [0, "
                    + definition.getMethodDefinition().getParameterDefinitions().length + "]");
        }

        this.definition.getMethodDefinition().getParameterDefinition(index).setType(type);
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
        ParameterDefinition definition = this.definition.getMethodDefinition().getParameterDefinition(name);

        if (definition == null) {
            throw new UnrulyException("No such parameter [" + name + "] found");
        }

        definition.setType(type);
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
        return ActionUtils.create(definition, null);
    }

    public ActionConsumer getAction() {
        return action;
    }

    public ActionDefinition getDefinition() {
        return definition;
    }
}
