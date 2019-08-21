package org.algorithmx.rules.core;

public interface RuleSet extends Iterable<Rule> {

    String getName();

    String getDescription();

    Rule getRule(String ruleName);

    int size();

    Rule[] getRules();

    RuleSet add(Class<?> ruleClass);

    RuleSet add(Rule rule);

    RuleSet add(String name, Rule rule);

    RuleSet add(RuleSet ruleSet);
}

