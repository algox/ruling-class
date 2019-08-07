package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.*;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;

import static org.algorithmx.rules.util.RuleUtils.load;

public interface RuleFactory {

    static RuleFactory defaultFactory() {
        return new DefaultRuleFactory(new DefaultObjectFactory());
    }

    IdentifiableRule rule(RuleDefinition ruleDefinition);

    default CompositeRule and(RuleSet ruleSet) {
        return and(ruleSet.getRules());
    }

    default CompositeRule and(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (!rule.isPass(ctx)) return false;
            }
            return true;
        });
    }

    default CompositeRule or(RuleSet ruleSet) {
        return or(ruleSet.getRules());
    }

    default CompositeRule or(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.isPass(ctx)) return true;
            }
            return false;
        });
    }

    default CompositeRule none(RuleSet ruleSet) {
        return none(ruleSet.getRules());
    }

    default CompositeRule none(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.isPass(ctx)) return false;
            }
            return true;
        });
    }

    default RuleSet rules(String name) {
        return new DefaultRuleSet(name, null);
    }

    default RuleSet rules(String name, String description) {
        return new DefaultRuleSet(name, description);
    }

    default IdentifiableRule rule(Class<?> rulingClass) {
        return rule(RuleDefinition.load(rulingClass));
    }

    default RuleAction action(Class<?> rulingClassWithAction) {
        Rule rule = rule(RuleDefinition.load(rulingClassWithAction));
        Action action = new SimpleAction(ActionDefinition.load(rulingClassWithAction), rule.getTarget());
        return new SimpleRuleAction(rule, action);
    }

    default IdentifiableRule rule(String name, Condition.Condition0 condition, String description) {
        return rule(load(condition, name, description));
    }

    default <A> IdentifiableRule rule(String name, Condition.Condition1<A> condition, String description) {
        return rule(load(condition, name, description));
    }

    default <A, B> IdentifiableRule rule(String name, Condition.Condition2<A, B> condition, String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C> IdentifiableRule rule(String name, Condition.Condition3<A, B, C> condition, String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D> IdentifiableRule rule(String name, Condition.Condition4<A, B, C, D> condition, String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D, E> IdentifiableRule rule(String name, Condition.Condition5<A, B, C, D, E> condition, String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D, E, F> IdentifiableRule rule(String name, Condition.Condition6<A, B, C, D, E, F> condition,
                                                             String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D, E, F, G> IdentifiableRule rule(String name, Condition.Condition7<A, B, C, D, E, F, G> condition,
                                                                String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D, E, F, G, H> IdentifiableRule rule(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition,
                                                                   String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D, E, F, G, H, I> IdentifiableRule rule(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition,
                                                                      String description) {
        return rule(load(condition, name, description));
    }

    default <A, B, C, D, E, F, G, H, I, J> IdentifiableRule rule(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition,
                                                                         String description) {
        return rule(load(condition, name, description));
    }

}
