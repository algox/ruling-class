package org.algorithmx.rules.core;

import org.algorithmx.rules.spring.util.Assert;

public interface RuleSet extends Iterable<RuleAction> {

    String getName();

    String getDescription();

    RuleAction get(String ruleName);

    IdentifiableRule getRule(String ruleName);

    int size();

    Rule[] getRules();

    RuleSet add(Class<?> ruleActionClass);

    RuleSet add(RuleAction ruleAction);

    default RuleSet add(RuleSet ruleSet) {
        Assert.notNull(ruleSet, "rules cannot be null.");
        // TODO
        return this;
    }
}

