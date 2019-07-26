package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

import static org.algorithmx.rules.util.RuleUtils.load;

public class DefaultRuleSet implements RuleSet {

    private static final String RULE_NAME_REGEX     = "^[a-zA-Z][a-zA-Z0-9]*?$";
    private static final Pattern NAME_PATTERN       = Pattern.compile(RULE_NAME_REGEX);

    private final String name;

    private final LinkedHashMap<String, RuleDefinition> rules = new LinkedHashMap<>();

    public DefaultRuleSet(String name) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "RuleSet name must match [" + NAME_PATTERN
                + "] Given [" + name + "]");
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public RuleDefinition get(String ruleName) {
        return rules.get(ruleName);
    }


    @Override
    public int size() {
        return rules.size();
    }

    @Override
    public RuleSet add(Class<?> c) {
        return add(RuleDefinition.load(c));
    }

    @Override
    public RuleSet add(String name, Condition.Condition0 condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A> RuleSet add(String name, Condition.Condition1<A> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B> RuleSet add(String name, Condition.Condition2<A, B> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C> RuleSet add(String name, Condition.Condition3<A, B, C> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D> RuleSet add(String name, Condition.Condition4<A, B, C, D> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D, E> RuleSet add(String name, Condition.Condition5<A, B, C, D, E> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D, E, F> RuleSet add(String name, Condition.Condition6<A, B, C, D, E, F> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D, E, F, G> RuleSet add(String name, Condition.Condition7<A, B, C, D, E, F, G> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D, E, F, G, H> RuleSet add(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> RuleSet add(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> RuleSet add(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition, String description) {
        return add(load(condition, name, description));
    }

    @Override
    public RuleSet add(RuleDefinition ruleDefinition) {
        RuleDefinition existingRule = rules.putIfAbsent(ruleDefinition.getName(), ruleDefinition);

        if (existingRule != null) {
            throw new UnrulyException("Rule with name [" + name + "] already exists in this Ruleset [" + getName() + "]");
        }

        return this;
    }

    @Override
    public Iterator<RuleDefinition> iterator() {
        return rules.values().iterator();
    }
}
