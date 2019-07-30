package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class DefaultRuleSet implements RuleSet {

    private static final String RULE_NAME_REGEX     = "^[a-zA-Z][a-zA-Z0-9]*?$";
    private static final Pattern NAME_PATTERN       = Pattern.compile(RULE_NAME_REGEX);

    private final String name;
    private final String description;

    private final LinkedHashMap<String, Rule> rules = new LinkedHashMap<>();

    public DefaultRuleSet(String name, String description) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "RuleSet name must match [" + NAME_PATTERN
                + "] Given [" + name + "]");
        this.name = name;
        this.description = description;
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
    public Rule get(String ruleName) {
        return rules.get(ruleName);
    }


    @Override
    public int size() {
        return rules.size();
    }

    @Override
    public RuleSet add(Rule rule) {
        Rule existingRule = rules.putIfAbsent(rule.getName(), rule);

        if (existingRule != null) {
            throw new UnrulyException("Rule with name [" + rule.getName()
                    + "] already exists in this Ruleset [" + getName() + "]");
        }

        return this;

    }

    @Override
    public RuleSet add(Class<?> c) {
        return add(RuleFactory.create(c));
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.values().iterator();
    }

    @Override
    public Rule[] getRules() {
        return rules.values().toArray(new Rule[size()]);
    }
}
