package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.*;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.*;

public class DefaultRuleSet implements RuleSet {

    private final String name;
    private final String description;
    private final RuleFactory ruleFactory;

    private final LinkedList<Rule> rules = new LinkedList<>();
    private final Map<String, Rule> ruleIndex = new HashMap<>();

    public DefaultRuleSet(String name, String description) {
        this(name, description, RuleFactory.defaultFactory());
    }

    public DefaultRuleSet(String name, String description, RuleFactory ruleFactory) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(RuleUtils.isValidRuleName(name), "RuleSet name must match [" + RuleUtils.RULE_NAME_REGEX
                + "] Given [" + name + "]");
        Assert.notNull(ruleFactory, "ruleFactory cannot be null.");
        this.name = name;
        this.description = description;
        this.ruleFactory = ruleFactory;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    @Override
    public Rule getRule(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        return ruleIndex.get(ruleName);
    }

    @Override
    public RuleSet add(Class<?> rulingClass) {
        add(ruleFactory.rule(rulingClass));
        return this;
    }

    @Override
    public RuleSet add(String name, Rule rule) {
        Assert.notNull(rule, "rule cannot be null.");
        Assert.isTrue(name == null || RuleUtils.isValidRuleName(name), "RuleSet name must match ["
                + RuleUtils.RULE_NAME_REGEX + "] Given [" + name + "]");

        if (name != null) {
            if (ruleIndex.containsKey(name)) {
                throw new UnrulyException("Rule with name [" + name + "] already exists in RuleSet [" + getName()
                        + "]. Existing Rule [" + ruleIndex.get(name).getRuleDefinition().getRulingClass().getName() + "]");
            }

            ruleIndex.put(name, rule);
        }

        rules.add(rule);

        return this;
    }

    @Override
    public RuleSet add(Rule rule) {
        return add(rule.isIdentifiable() ? ((Identifiable) rule).getName() : null, rule);
    }

    @Override
    public RuleSet add(Collection<Rule> rules) {
        Assert.notNull(rules, "rules cannot be null.");
        rules.stream().forEach(rule -> add(rule));
        return this;
    }

    @Override
    public int size() {
        return rules.size();
    }

    @Override
    public Rule[] getRules() {
        return rules.toArray(new Rule[rules.size()]);
    }
}
