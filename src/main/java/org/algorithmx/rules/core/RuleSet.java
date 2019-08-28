package org.algorithmx.rules.core;

import java.util.Collection;

public interface RuleSet extends Iterable<Rule> {

    String getName();

    String getDescription();

    Rule getRule(String ruleName);

    int size();

    Rule[] getRules();

    RuleSet add(Class<?> ruleClass);

    RuleSet add(Rule rule);

    RuleSet add(Collection<Rule> rules);

    RuleSet add(String name, Rule rule);
}

