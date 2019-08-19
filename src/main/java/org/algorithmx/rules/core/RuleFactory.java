package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultCompositeRule;
import org.algorithmx.rules.core.impl.DefaultObjectFactory;
import org.algorithmx.rules.core.impl.DefaultRuleFactory;
import org.algorithmx.rules.core.impl.DefaultRuleSet;
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

    default ActionableRule rule(Class<?> rulingClass) {
        return rule(RuleDefinition.load(rulingClass));
    }

    default ActionableRule rule(Condition condition) {
        return rule(load(condition, null, null));
    }

    default ActionableRule rule(Condition.Condition0 arg) {
        return rule((Condition) arg);
    }

    default <A> ActionableRule rule(Condition.Condition1<A> arg) {
        return rule((Condition) arg);
    }

    default <A, B> ActionableRule rule(Condition.Condition2<A, B> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C> ActionableRule rule(Condition.Condition3<A, B, C> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D> ActionableRule rule(Condition.Condition4<A, B, C, D> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E> ActionableRule rule(Condition.Condition5<A, B, C, D, E> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F> ActionableRule rule(Condition.Condition6<A, B, C, D, E, F> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G> ActionableRule rule(Condition.Condition7<A, B, C, D, E, F, G> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G, H> ActionableRule rule(Condition.Condition8<A, B, C, D, E, F, G, H> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G, H, I> ActionableRule rule(Condition.Condition9<A, B, C, D, E, F, G, H, I> arg) {
        return rule((Condition) arg);
    }

    default <A, B, C, D, E, F, G, H, I, J> ActionableRule rule(Condition.Condition10<A, B, C, D, E, F, G, H, I, J> arg) {
        return rule((Condition) arg);
    }

    default IdentifiableRule rule(String name, Condition condition) {
        return rule(load(condition, name, null));
    }

    default IdentifiableRule rule(String name, Condition condition, String description) {
        return rule(load(condition, name, description));
    }
}
