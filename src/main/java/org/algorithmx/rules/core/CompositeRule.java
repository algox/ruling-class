package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultCompositeRule;

public interface CompositeRule extends BaseRule {

    static CompositeRule all(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (!rule.run(ctx)) return false;
            }
           return true;
        });
    }

    static CompositeRule any(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.run(ctx)) return true;
            }
            return false;
        });
    }

    static CompositeRule none(final Rule[] allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.run(ctx)) return false;
            }
            return true;
        });
    }

    Rule[] getRules();
}
