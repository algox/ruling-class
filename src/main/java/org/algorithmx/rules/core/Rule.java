package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.SimpleAction;
import org.algorithmx.rules.core.impl.SimpleRuleAction;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.util.ActionUtils;

import java.util.Objects;
import java.util.function.Predicate;

@FunctionalInterface
public interface Rule extends Predicate<RuleExecutionContext> {

    boolean isPass(RuleExecutionContext ctx) throws UnrulyException;

    default boolean isPass(BindingDeclaration... bindings) {
        return isPass(Bindings.defaultBindings().bind(bindings));
    }

    default boolean isPass(Bindings bindings) throws UnrulyException {
        return isPass(RuleExecutionContext.create(bindings));
    }

    default boolean isFail(RuleExecutionContext ctx) throws UnrulyException {
        return !isPass(ctx);
    }

    default boolean isFail(BindingDeclaration... bindings) {
        return !isPass(Bindings.defaultBindings().bind(bindings));
    }

    default boolean isFail(Bindings bindings) throws UnrulyException {
        return !isPass(RuleExecutionContext.create(bindings));
    }

    default boolean test(Bindings bindings) throws UnrulyException {
        return isPass(RuleExecutionContext.create(bindings));
    }

    default boolean test(RuleExecutionContext ctx) throws UnrulyException {
        return isPass(ctx);
    }

    default Rule and(Rule other) {
        Objects.requireNonNull(other);
        return (t) -> isPass(t) && other.test(t);
    }

    default Rule negate() {
        return (t) -> !test(t);
    }

    default Rule or(Rule other) {
        Objects.requireNonNull(other);
        return (t) -> test(t) || other.test(t);
    }

    default RuleAction then(Action action) {
        return new SimpleRuleAction(this, action);
    }

    default RuleAction then(Class<?> actionClass) {
        return new SimpleRuleAction(this, new SimpleAction(ActionDefinition.load(actionClass), getTarget()));
    }

    default RuleAction then(Then.Then0 action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A> RuleAction then(Then.Then1<A> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B> RuleAction then(Then.Then2<A, B> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C> RuleAction then(Then.Then3<A, B, C> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D> RuleAction then(Then.Then4<A, B, C, D> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D, E> RuleAction then(Then.Then5<A, B, C, D, E> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D, E, F> RuleAction then(Then.Then6<A, B, C, D, E, F> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D, E, F, G> RuleAction then(Then.Then7<A, B, C, D, E, F, G> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D, E, F, G, H> RuleAction then(Then.Then8<A, B, C, D, E, F, G, H> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D, E, F, G, H, I> RuleAction then(Then.Then9<A, B, C, D, E, F, G, H, I> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default <A, B, C, D, E, F, G, H, I, J> RuleAction then(Then.Then10<A, B, C, D, E, F, G, H, I, J> action) {
        return new SimpleRuleAction(this, ActionUtils.create(action, null, getTarget()));
    }

    default Object getTarget() {
        return null;
    }
}
