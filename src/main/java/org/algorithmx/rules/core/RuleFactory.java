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

    default Rule rule(Condition condition) {
        return rule(load(condition, null, null));
    }

    default IdentifiableRule rule(String name, Condition condition) {
        return rule(load(condition, name, null));
    }

    default IdentifiableRule rule(String name, Condition condition, String description) {
        return rule(load(condition, name, description));
    }

    default RuleAction action(Class<?> rulingClassWithAction) {
        Rule rule = rule(RuleDefinition.load(rulingClassWithAction));
        Action action = new SimpleAction(ActionDefinition.load(rulingClassWithAction), rule.getTarget());
        return new SimpleRuleAction(rule, action);
    }

    default RuleAction action(Condition condition, Action...actions) {
        return new SimpleRuleAction(rule(condition), actions);
    }

    default RuleAction action(String ruleName, Condition condition, Action...actions) {
        return new SimpleRuleAction(rule(ruleName, condition, null), actions);
    }

    default RuleAction action(String ruleName, String description, Condition condition, Action...actions) {
        return new SimpleRuleAction(rule(ruleName, condition, description), actions);
    }
}
