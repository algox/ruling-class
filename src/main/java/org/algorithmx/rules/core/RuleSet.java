package org.algorithmx.rules.core;

import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.core.impl.DefaultRuleSet;

public interface RuleSet extends Iterable<Rule> {

    static RuleSet create(String name) {
        return new DefaultRuleSet(name, null);
    }

    static RuleSet create(String name, String description) {
        return new DefaultRuleSet(name, description);
    }

    String getName();

    String getDescription();

    Rule get(String ruleName);

    int size();

    RuleSet add(Rule rule);

    RuleSet add(RuleDefinition ruleDefinition);

    RuleSet add(Class<?> rulingClass);
}

