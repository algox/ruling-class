package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.DefaultCompositeRule;

public interface CompositeRule extends Rule {

    static CompositeRule AND(final Rule...allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (!rule.isPass(ctx)) return false;
            }
            return true;
        });
    }

    static CompositeRule OR(final Rule...allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.isPass(ctx)) return true;
            }
            return false;
        });
    }

    static CompositeRule NONE(final Rule...allRules) {
        return new DefaultCompositeRule(allRules, (rules, ctx) -> {
            for (Rule rule : rules) {
                if (rule.isPass(ctx)) return false;
            }
            return true;
        });
    }

    Rule[] getRules();
}
