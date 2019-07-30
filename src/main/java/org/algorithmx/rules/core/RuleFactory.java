package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultCompositeRule;
import org.algorithmx.rules.core.impl.SimpleRule;
import org.algorithmx.rules.model.RuleDefinition;

import static org.algorithmx.rules.util.RuleUtils.load;

public final class RuleFactory {

    private RuleFactory() {
        super();
    }

    public static Rule create(Class<?> rulingClass) {
        return create(RuleDefinition.load(rulingClass));
    }

    public static CompositeRule and(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (!rule.run(ctx)) return false;
            }
            return true;
        });
    }

    public static CompositeRule and(RuleSet ruleSet) {
        return and(ruleSet.getRules());
    }

    public static CompositeRule or(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.run(ctx)) return true;
            }
            return false;
        });
    }

    public static CompositeRule or(RuleSet ruleSet) {
        return or(ruleSet.getRules());
    }

    public static CompositeRule none(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.run(ctx)) return false;
            }
            return true;
        });
    }

    public static CompositeRule none(RuleSet ruleSet) {
        return none(ruleSet.getRules());
    }

    public static Rule create(String name, Condition.Condition0 condition, String description) {
        return create(load(condition, name, description));
    }

    public static <A> Rule create(String name, Condition.Condition1<A> condition, String description) {
        return create(load(condition, name, description));
    }

    public static <A, B> Rule create(String name, Condition.Condition2<A, B> condition, String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C> Rule create(String name, Condition.Condition3<A, B, C> condition, String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D> Rule create(String name, Condition.Condition4<A, B, C, D> condition, String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D, E> Rule create(String name, Condition.Condition5<A, B, C, D, E> condition, String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D, E, F> Rule create(String name, Condition.Condition6<A, B, C, D, E, F> condition,
                                                             String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D, E, F, G> Rule create(String name, Condition.Condition7<A, B, C, D, E, F, G> condition,
                                                                String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D, E, F, G, H> Rule create(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition,
                                                                   String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D, E, F, G, H, I> Rule create(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition,
                                                                      String description) {
        return create(load(condition, name, description));
    }

    public static <A, B, C, D, E, F, G, H, I, J> Rule create(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition,
                                                                         String description) {
        return create(load(condition, name, description));
    }

    private static Rule create(RuleDefinition ruleDefinition) {
        return new SimpleRule(ruleDefinition);
    }

}
