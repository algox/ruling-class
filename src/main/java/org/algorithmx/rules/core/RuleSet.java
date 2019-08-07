package org.algorithmx.rules.core;

import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;

public interface RuleSet extends Iterable<IdentifiableRule> {

    String getName();

    String getDescription();

    Rule get(String ruleName);

    int size();

    RuleSet add(IdentifiableRule rule);

    RuleSet add(Class<?> rulingClass);

    IdentifiableRule[] getRules();

    default RuleSet add(IdentifiableRule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        Arrays.stream(rules).forEach(this::add);
        return this;
    }

    default RuleSet add(RuleSet ruleSet) {
        Assert.notNull(ruleSet, "rules cannot be null.");
        add(ruleSet.getRules());
        return this;
    }

    default RuleSet add(String name, Condition.Condition0 condition, String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A> RuleSet add(String name, Condition.Condition1<A> condition, String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B> RuleSet add(String name, Condition.Condition2<A, B> condition, String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C> RuleSet add(String name, Condition.Condition3<A, B, C> condition, String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D> RuleSet add(String name, Condition.Condition4<A, B, C, D> condition, String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D, E> RuleSet add(String name, Condition.Condition5<A, B, C, D, E> condition, String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D, E, F> RuleSet add(String name, Condition.Condition6<A, B, C, D, E, F> condition,
                                                 String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D, E, F, G> RuleSet add(String name, Condition.Condition7<A, B, C, D, E, F, G> condition,
                                                    String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D, E, F, G, H> RuleSet add(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition,
                                                       String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D, E, F, G, H, I> RuleSet add(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition,
                                                          String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }

    default <A, B, C, D, E, F, G, H, I, J> RuleSet add(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition,
                                                             String description) {
        //add(RuleFactory.rule(name, condition, description));
        return this;
    }
}

