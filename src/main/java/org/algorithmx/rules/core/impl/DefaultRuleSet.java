package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.ActionableRule;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.*;
import java.util.regex.Pattern;

public class DefaultRuleSet implements RuleSet {

    private final String name;
    private final String description;
    private final RuleFactory ruleFactory;

    private final LinkedList<ActionableRule> actions = new LinkedList<>();
    private final Map<String, ActionableRule> ruleIndex = new HashMap<>();

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
    public Iterator<ActionableRule> iterator() {
        return actions.iterator();
    }

    @Override
    public ActionableRule getRule(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        return ruleIndex.get(ruleName);
    }

    @Override
    public RuleSet add(Class<?> rulingClass) {
        add(ruleFactory.rule(rulingClass));
        return this;
    }

    @Override
    public RuleSet add(String name, ActionableRule rule) {
        Assert.notNull(rule, "rule cannot be null.");
        Assert.isTrue(name == null || RuleUtils.isValidRuleName(name), "RuleSet name must match ["
                + RuleUtils.RULE_NAME_REGEX + "] Given [" + name + "]");

        actions.add(rule);

        if (name != null) {
            ruleIndex.put(name, rule);
        }

        return this;
    }

    @Override
    public final RuleSet add(ActionableRule rule) {
        return add(rule.isIdentifiable() ? ((Identifiable) rule).getName() : null, rule);
    }

    @Override
    public RuleSet add(RuleSet ruleSet) {
        Assert.notNull(ruleSet, "ruleSet cannot be null.");
        Arrays.stream(ruleSet.getRules()).forEach((ActionableRule rule) -> add(rule));
        return this;
    }

    @Override
    public int size() {
        return actions.size();
    }

    @Override
    public ActionableRule[] getRules() {
        return actions.toArray(new ActionableRule[actions.size()]);
    }
}
