package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.*;
import org.algorithmx.rules.spring.util.Assert;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Pattern;

public class DefaultRuleSet implements RuleSet {

    private static final String RULE_NAME_REGEX     = "^[a-zA-Z][a-zA-Z0-9]*?$";
    private static final Pattern NAME_PATTERN       = Pattern.compile(RULE_NAME_REGEX);

    private final String name;
    private final String description;
    private final RuleFactory ruleFactory;

    private final LinkedList<RuleAction> actions = new LinkedList<>();
    private final Map<String, RuleAction> ruleIndex = new HashMap<>();

    public DefaultRuleSet(String name, String description) {
        this(name, description, RuleFactory.defaultFactory());
    }

    public DefaultRuleSet(String name, String description, RuleFactory ruleFactory) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "RuleSet name must match [" + NAME_PATTERN
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
    public Iterator<RuleAction> iterator() {
        return actions.iterator();
    }

    @Override
    public IdentifiableRule getRule(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        RuleAction ruleAction = ruleIndex.get(ruleName);

        return ruleAction != null
                ? (IdentifiableRule) ruleAction.getRule()
                : null;

    }

    @Override
    public RuleAction get(String ruleName) {
        return ruleIndex.get(ruleName);
    }

    @Override
    public RuleSet add(Class<?> ruleActionClass) {
        add(ruleFactory.action(ruleActionClass));
        return this;
    }

    @Override
    public final RuleSet add(RuleAction ruleAction) {
        actions.add(ruleAction);

        if (ruleAction.getRule().isIdentifiable()) {
            ruleIndex.put(((Identifiable) ruleAction.getRule()).getName(), ruleAction);
        }

        return this;
    }

    @Override
    public int size() {
        return actions.size();
    }

    @Override
    public Rule[] getRules() {
        actions.stream().forEach((RuleAction r) -> r.getRule());
        return null;
    }
}
