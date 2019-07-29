package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.impl.SimpleRule;
import org.algorithmx.rules.model.RuleDefinition;

import java.util.concurrent.atomic.AtomicLong;

import static org.algorithmx.rules.util.RuleUtils.load;

public interface Rule extends BaseRule {

    AtomicLong ID = new AtomicLong(System.currentTimeMillis());

    static Rule create(RuleDefinition ruleDefinition) {
        return new SimpleRule(ruleDefinition);
    }

    static Rule create(Class<?> rulingClass) {
        return create(RuleDefinition.load(rulingClass));
    }

    static Rule create(Condition.Condition0 condition) {
        return create(load(condition, "anonymous-rule0-" + ID.incrementAndGet(), null));
    }

    static Rule create(String name, Condition.Condition0 condition, String description) {
        return create(load(condition, name, description));
    }

    static <A> Rule create(Condition.Condition1<A> condition) {
        return create(load(condition, "anonymous-rule-1" + ID.incrementAndGet(), null));
    }

    static <A> Rule create(String name, Condition.Condition1<A> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B> Rule create(Condition.Condition2<A, B> condition) {
        return create(load(condition, "anonymous-rule-2" + ID.incrementAndGet(), null));
    }

    static <A, B> Rule create(String name, Condition.Condition2<A, B> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C> Rule create(Condition.Condition3<A, B, C> condition) {
        return create(load(condition, "anonymous-rule-3" + ID.incrementAndGet(), null));
    }

    static <A, B, C> Rule create(String name, Condition.Condition3<A, B, C> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D> Rule create(Condition.Condition4<A, B, C, D> condition) {
        return create(load(condition, "anonymous-rule-4" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D> Rule create(String name, Condition.Condition4<A, B, C, D> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D, E> Rule create(Condition.Condition5<A, B, C, D, E> condition) {
        return create(load(condition, "anonymous-rule-5" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D, E> Rule create(String name, Condition.Condition5<A, B, C, D, E> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D, E, F> Rule create(Condition.Condition6<A, B, C, D, E, F> condition) {
        return create(load(condition, "anonymous-rule-6" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D, E, F> Rule create(String name, Condition.Condition6<A, B, C, D, E, F> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D, E, F, G> Rule create(Condition.Condition7<A, B, C, D, E, F, G> condition) {
        return create(load(condition, "anonymous-rule-7" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D, E, F, G> Rule create(String name, Condition.Condition7<A, B, C, D, E, F, G> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D, E, F, G, H> Rule create(Condition.Condition8<A, B, C, D, E, F, G, H> condition) {
        return create(load(condition, "anonymous-rule-8" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D, E, F, G, H> Rule create(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D, E, F, G, H, I> Rule create(Condition.Condition9<A, B, C, D, E, F, G, H, I> condition) {
        return create(load(condition, "anonymous-rule-8" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D, E, F, G, H, I> Rule create(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition, String description) {
        return create(load(condition, name, description));
    }

    static <A, B, C, D, E, F, G, H, I, J> Rule create(Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition) {
        return create(load(condition, "anonymous-rule-10" + ID.incrementAndGet(), null));
    }

    static <A, B, C, D, E, F, G, H, I, J> Rule create(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition, String description) {
        return create(load(condition, name, description));
    }

    default boolean run(Object... args) throws UnrulyException {
        return run(RuleExecutionContext.create(), args);
    }

    default RuleDefinition getRuleDefinition() {
        throw new UnrulyException("Rule implementation must return the RuleDefinition!");
    }

    boolean run(RuleExecutionContext ctx, Object... args) throws UnrulyException;
}
