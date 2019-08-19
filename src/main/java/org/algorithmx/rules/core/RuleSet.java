package org.algorithmx.rules.core;

public interface RuleSet extends Iterable<ActionableRule> {

    String getName();

    String getDescription();

    ActionableRule getRule(String ruleName);

    int size();

    ActionableRule[] getRules();

    RuleSet add(Class<?> ruleActionClass);

    RuleSet add(ActionableRule rule);

    RuleSet add(String name, ActionableRule rule);

    RuleSet add(RuleSet ruleSet);
}

