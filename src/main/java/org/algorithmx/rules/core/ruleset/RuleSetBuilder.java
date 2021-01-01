/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Pattern;

public class RuleSetBuilder {

    private static final Pattern NAME_PATTERN = Pattern.compile(Rule.NAME_REGEX);

    private final String name;
    private String description;
    private final LinkedList<Rule> rules = new LinkedList<>();

    private Condition preCondition;
    private Action preAction;
    private Action postAction;
    private Condition stopCondition;

    private Condition errorHandler;

    private RuleSetBuilder(String name) {
        this(name, null);
    }

    private RuleSetBuilder(String name, String description) {
        super();
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "RuleSet name must match [" + NAME_PATTERN + "]");
        this.name = name;
        this.description = description;
    }

    public static RuleSetBuilder with(String name) {
        return new RuleSetBuilder(name);
    }

    public static RuleSetBuilder with(String name, String description) {
        return new RuleSetBuilder(name, description);
    }

    public RuleSetBuilder description(String description) {
        this.description = description;
        return this;
    }

    public RuleSetBuilder clear() {
        this.rules.clear();
        return this;
    }

    public Rule get(int index) {
        return rules.get(index);
    }

    public Rule get(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        Rule result = null;

        for (Rule rule : rules) {
            if (ruleName.equals(rule.getName())) {
                result = rule;
                break;
            }
        }

        return result;
    }

    public Rule[] getAll(String ruleName) {
        Assert.notNull(ruleName, "ruleName cannot be null.");
        List<Rule> result = null;

        for (Rule rule : rules) {
            if (ruleName.equals(rule.getName())) {
                result.add(rule);
                break;
            }
        }

        return result.toArray(new Rule[result.size()]);
    }

    public int getIndex(Rule rule) {
        Assert.notNull(rule, "Rule cannot be null.");
        Integer result = null;

        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i).equals(rule)) {
                result = i;
                break;
            }
        }

        if (result == null) throw new NoSuchElementException("Rule [" + rule + "] not found");

        return result;
    }

    public RuleSetBuilder addBefore(Rule rule, Rule existingRule) {
        Assert.notNull(rule, "Rule cannot be null.");
        Assert.notNull(existingRule, "existingRule cannot be null.");
        int index = getIndex(existingRule);
        rules.add(index, rule);
        return this;
    }

    public RuleSetBuilder addAfter(Rule rule, Rule existingRule) {
        Assert.notNull(rule, "Rule cannot be null.");
        Assert.notNull(existingRule, "existingRule cannot be null.");
        int index = getIndex(existingRule);
        rules.add(index + 1, rule);
        return this;
    }

    public RuleSetBuilder addFirst(Rule rule) {
        Assert.notNull(rule, "Rule cannot be null.");
        this.rules.addFirst(rule);
        return this;
    }

    public RuleSetBuilder add(Rule rule) {
        Assert.notNull(rule, "Rule cannot be null.");
        this.rules.add(rule);
        return this;
    }

    public RuleSetBuilder addAll(Rule...rules) {
        Assert.notNull(rules, "Rules cannot be null.");
        addAll(Arrays.asList(rules));
        return this;
    }

    public RuleSetBuilder addAll(Collection<Rule> rules) {
        Assert.notNull(rules, "Rules cannot be null.");
        this.rules.addAll(rules);
        return this;
    }

    /**
     * PreCondition(Optional) Condition to be met before the execution of the RuleSet.
     *
     * @param preCondition pre check before execution of the RuleSet.
     * @return this for fluency.
     */
    public RuleSetBuilder preCondition(Condition preCondition) {
        this.preCondition = preCondition;
        return this;
    }

    /**
     * PreAction(Optional) to be performed before the execution of the Rules.
     *
     * @param action pre action before the execution of the RuleSet.
     * @return this for fluency.
     */
    public RuleSetBuilder preAction(Action action) {
        this.preAction = action;
        return this;
    }

    /**
     * PostAction(Optional) to be performed after the execution of the Rules.
     *
     * @param action post action after the execution of the RuleSet.
     * @return this for fluency.
     */
    public RuleSetBuilder postAction(Action action) {
        this.postAction = action;
        return this;
    }

    /**
     * Condition that determines when execution should stop.
     *
     * @param condition stopping condition.
     * @return this for fluency.
     */
    public RuleSetBuilder stopWhen(Condition condition) {
        this.stopCondition = condition;
        return this;
    }

    public RuleSetBuilder errorHandler(Condition errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }

    public RuleSetDefinition buildRuleSetDefinition() {
        List<RuleDefinition> ruleDefinitions = new ArrayList<>(rules.size());

        rules.stream().forEach(r -> ruleDefinitions.add(r.getRuleDefinition()));

        RuleSetDefinition result = new RuleSetDefinition(getName(), getDescription(),
                getPreCondition() != null ? getPreCondition().getMethodDefinition() : null,
                getPreAction() != null ? getPreAction().getMethodDefinition() : null,
                getPostAction() != null ? getPostAction().getMethodDefinition() : null,
                getStopCondition() != null ? getStopCondition().getMethodDefinition() : null,
                getErrorHandler() != null ? getErrorHandler().getMethodDefinition() : null,
                ruleDefinitions.toArray(new RuleDefinition[ruleDefinitions.size()]));

        return result;
    }

    public RuleSet build() {
        return new DefaultRuleSet(buildRuleSetDefinition(),
                getPreCondition(), getPreAction(), getPostAction(), getStopCondition(), errorHandler,
                rules.toArray(new Rule[rules.size()]));
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Condition getPreCondition() {
        return preCondition;
    }

    public Action getPreAction() {
        return preAction;
    }

    public Action getPostAction() {
        return postAction;
    }

    public Condition getStopCondition() {
        return stopCondition;
    }

    public Condition getErrorHandler() {
        return errorHandler;
    }

    public Rule[] getRules() {
        return rules.toArray(new Rule[rules.size()]);
    }
}
