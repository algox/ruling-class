package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.spring.util.Assert;

import java.util.function.Predicate;

public interface Rule extends Predicate<RuleExecutionContext> {

    default void run(RuleExecutionContext ctx) throws UnrulyException {
        if (isPass(ctx)) {
            for (Action action : getActions()) {
                action.run(ctx);
            }
        }
    }

    Action[] getActions();

    Rule then(Action action);

    Rule then(Then action);

    Rule then(Then action, String description);

    Rule then(Then.Then0 arg);

    <A> Rule then(Then.Then1<A> arg);

    <A, B> Rule then(Then.Then2<A, B> arg);

    <A, B, C> Rule then(Then.Then3<A, B, C> arg);

    <A, B, C, D> Rule then(Then.Then4<A, B, C, D> arg);

    <A, B, C, D, E> Rule then(Then.Then5<A, B, C, D, E> arg);

    <A, B, C, D, E, F> Rule then(Then.Then6<A, B, C, D, E, F> arg);

    <A, B, C, D, E, F, G> Rule then(Then.Then7<A, B, C, D, E, F, G> arg);

    <A, B, C, D, E, F, G, H> Rule then(Then.Then8<A, B, C, D, E, F, G, H> arg);

    <A, B, C, D, E, F, G, H, I> Rule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg);

    <A, B, C, D, E, F, G, H, I, J> Rule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg);

    default void run(BindingDeclaration...bindings) {
        run(Bindings.simpleBindings().bind(bindings));
    }

    default void run(Bindings bindings) throws UnrulyException {
        run(RuleExecutionContext.create(bindings));
    }

    boolean isPass(RuleExecutionContext ctx) throws UnrulyException;

    default boolean isPass(BindingDeclaration... bindings) {
        return isPass(Bindings.simpleBindings().bind(bindings));
    }

    default boolean isPass(Bindings bindings) throws UnrulyException {
        return isPass(RuleExecutionContext.create(bindings));
    }

    default boolean isFail(RuleExecutionContext ctx) throws UnrulyException {
        return !isPass(ctx);
    }

    default boolean isFail(BindingDeclaration... bindings) {
        return !isPass(Bindings.simpleBindings().bind(bindings));
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

    default boolean isIdentifiable() {
        return this instanceof Identifiable;
    }

    default Rule and(Rule other) {
        Rule[] rules = new Rule[1];
        rules[0] = other;
        return and(rules);
    }

    default Rule and(Rule...others) {
        return CompositeRule.AND(combine(this, others));
    }

    default Rule or(Rule other) {
        Rule[] rules = new Rule[1];
        rules[0] = other;
        return or(rules);
    }

    default Rule or(Rule...others) {
        return CompositeRule.OR(combine(this, others));
    }

    default Rule none(Rule other) {
        Rule[] rules = new Rule[1];
        rules[0] = other;
        return none(rules);
    }

    default Rule none(Rule...others) {
        return CompositeRule.NONE(combine(this, others));
    }

    Object getTarget();

    static Rule[] combine(Rule rule, Rule[] others) {
        Assert.isTrue(others != null && others.length > 0,
                "others cannot be null and must have at least 1 element");
        Rule[] result = new Rule[others.length + 1];
        result[0] = rule;
        System.arraycopy(others, 0, result, 1, others.length);
        return result;
    }
}
