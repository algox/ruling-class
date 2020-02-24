/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.build.RuleBuilder;
import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

/**
 * Default implementation of the RuleSet.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultRuleSet implements RuleSet {

    private final String name;
    private final String description;

    private Condition preCondition = null;
    private Condition stopCondition = null;
    private Action preAction = null;
    private Action postAction = null;

    private final LinkedList<Rule> rules = new LinkedList<>();
    private final Map<String, Rule> ruleIndex = new HashMap<>();

    public DefaultRuleSet(String name, String description) {
        super();
        Assert.notNull(name, "name cannot be null.");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(RuleUtils.isValidRuleName(name), "RuleSet name must match [" + RuleUtils.RULE_NAME_REGEX
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
    public Iterator<Rule> iterator() {
        return rules.iterator();
    }

    @Override
    public Rule getRule(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        return ruleIndex.get(ruleName);
    }

    public Rule getRule(Class<?> ruleClass) {
        Assert.notNull(ruleClass, "ruleName cannot be null.");
        Rule result = null;

        for (Rule rule : rules) {
            if (ruleClass.equals(rule.getRuleDefinition().getRuleClass())) {
                result = rule;
                break;
            }
        }

        return result;
    }

    @Override
    public RuleSet add(String name, Rule rule) {

        Assert.notNull(rule, "rule cannot be null.");
        Assert.isTrue(name == null || RuleUtils.isValidRuleName(name), "RuleSet name must match ["
                + RuleUtils.RULE_NAME_REGEX + "] Given [" + name + "]");

        if (name != null) {
            if (ruleIndex.containsKey(name)) {
                throw new UnrulyException("Rule with name [" + name + "] already exists in RuleSet [" + getName()
                        + "]. Existing Rule [" + ruleIndex.get(name).getRuleDefinition().getRuleClass().getName() + "]");
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
    public RuleSet add(Class<?> ruleClass) {
        add(RuleBuilder.withClass(ruleClass).build());
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

    @Override
    public RuleSet remove(String... ruleNames) {
        Assert.notNull(ruleNames, "ruleNames cannot be null.");

        for (String ruleName : ruleNames) {
            Rule rule = getRule(ruleName);

            if (rule == null) {
                throw new UnrulyException("Rule [" + ruleName + "] not found in RuleSet [" + name + "]");
            }

            rules.remove(rule);
            ruleIndex.remove(ruleName);
        }

        return this;
    }

    @Override
    public RuleSet remove(Class<?>... ruleClasses) {
        Assert.notNull(ruleClasses, "ruleClasses cannot be null.");

        for (Class<?> ruleClass : ruleClasses) {
            Rule rule = getRule(ruleClass);

            if (rule == null) {
                throw new UnrulyException("Rule Class [" + ruleClass + "] not found in RuleSet [" + name + "]");
            }

            rules.remove(rule);
            ruleIndex.remove(rule.getRuleDefinition().getName());
        }

        return this;
    }

    @Override
    public void preCondition(Condition condition) {
        this.preCondition = condition;
    }

    @Override
    public void preAction(Action action) {
        this.preAction = action;
    }

    @Override
    public void postAction(Action action) {
        this.postAction = action;
    }

    @Override
    public void stopWhen(Condition condition) {
        this.stopCondition = condition;
    }

    @Override
    public Condition getPreCondition() {
        return preCondition;
    }

    @Override
    public Action getPreAction() {
        return preAction;
    }

    @Override
    public Action getPostAction() {
        return postAction;
    }

    @Override
    public Condition getStopCondition() {
        return stopCondition;
    }

    @Override
    public String toString() {
        return "DefaultRuleSet{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", rules='" + rules + '\'' +
                '}';
    }
}
