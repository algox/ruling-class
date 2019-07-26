package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultRuleFactory;
import org.algorithmx.rules.model.RuleDefinition;

import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

import static org.algorithmx.rules.util.RuleUtils.load;

public interface RuleFactory {

    AtomicLong ID = new AtomicLong(System.currentTimeMillis());

    static RuleFactory create() {
        return new DefaultRuleFactory();
    }

    default Rule rule(Class<?> rulingClass) {
        return Rule.create(RuleDefinition.load(rulingClass));
    }

    default Rule rule(RuleDefinition ruleDefinition) {
        return Rule.create(ruleDefinition);
    }

    default Rule rule(RuleSet ruleSet, String ruleName) {
        RuleDefinition ruleDefinition = ruleSet.get(ruleName);
        return ruleDefinition != null ? Rule.create(ruleDefinition) : null;
    }

    CompositeRule all(RuleSet ruleSet);

    CompositeRule any(RuleSet ruleSet);

    CompositeRule none(RuleSet ruleSet);

    default Rule rule(Condition.Condition0 condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A> Rule rule(Condition.Condition1<A> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B> Rule rule(Condition.Condition2<A, B> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C> Rule rule(Condition.Condition3<A, B, C> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D> Rule rule(Condition.Condition4<A, B, C, D> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D, E> Rule rule(Condition.Condition5<A, B, C, D, E> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D, E, F> Rule rule(Condition.Condition6<A, B, C, D, E, F> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D, E, F, G> Rule rule(Condition.Condition7<A, B, C, D, E, F, G> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D, E, F, G, H> Rule rule(Condition.Condition8<A, B, C, D, E, F, G, H> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D, E, F, G, H, I> Rule rule(Condition.Condition9<A, B, C, D, E, F, G, H, I> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }

    default <A, B, C, D, E, F, G, H, I, J> Rule rule(Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition) {
        return Rule.create(load(condition, "anonymous-lambda-" + ID.incrementAndGet(), null));
    }
}
