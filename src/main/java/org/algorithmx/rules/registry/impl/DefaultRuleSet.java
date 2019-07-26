package org.algorithmx.rules.registry.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.registry.RuleSet;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.LambdaUtils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.regex.Pattern;

public class DefaultRuleSet implements RuleSet {

    private static final String RULE_NAME_REGEX     = "^[a-zA-Z][a-zA-Z0-9]*?$";
    private static final Pattern NAME_PATTERN       = Pattern.compile(RULE_NAME_REGEX);

    private final String name;

    private final LinkedHashMap<String, Rule> rules = new LinkedHashMap<>();

    public DefaultRuleSet(String name) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "RuleSet name must match [" + NAME_PATTERN
                + "] Given [" + name + "]");
        this.name = name;
    }

    @Override
    public boolean run(RuleExecutionContext ctx) throws UnrulyException {
        return false;
    }

    @Override
    public Rule add(Class<?> c) {
        RuleDefinition ruleDefinition = RuleDefinition.load(c);
        Rule result = Rule.create(ruleDefinition);
        add(result, ruleDefinition.getName());
        return result;
    }

    @Override
    public Rule add(String name, Condition.Condition0 condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A> Rule add(String name, Condition.Condition1<A> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B> Rule add(String name, Condition.Condition2<A, B> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C> Rule add(String name, Condition.Condition3<A, B, C> condition, String description) {
        return add(condition, name, description);
    }

    @Override
    public <A, B, C, D> Rule add(String name, Condition.Condition4<A, B, C, D> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C, D, E> Rule add(String name, Condition.Condition5<A, B, C, D, E> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C, D, E, F> Rule add(String name, Condition.Condition6<A, B, C, D, E, F> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C, D, E, F, G> Rule add(String name, Condition.Condition7<A, B, C, D, E, F, G> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C, D, E, F, G, H> Rule add(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> Rule add(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> Rule add(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition, String description) {
        Rule result = add(condition, name, description);
        add(result, name);
        return result;
    }

    public void add(Rule rule, String name) {
        Rule existingRule = rules.putIfAbsent(name, rule);

        if (existingRule != null) {
            throw new UnrulyException("Rule with name [" + name + "] already exists in this Ruleset [" + getName() + "]");
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Rule get(String ruleName) {
        return rules.get(ruleName);
    }

    @Override
    public Iterator<Rule> iterator() {
        return rules.values().iterator();
    }

    protected Rule add(Condition condition, String name, String description) {
        RuleDefinition ruleDefinition = RuleDefinition.load(LambdaUtils.getSerializedLambda(condition), name, description);
        return Rule.create(ruleDefinition);
    }
}
