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

import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.validation.ValidationRule;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class RuleSetBuilder {

    private static final Pattern NAME_PATTERN = Pattern.compile(Rule.NAME_REGEX);

    private final String name;
    private String description;
    private RuleSet.ORDER order = RuleSet.ORDER.IN_ORDER;
    private final LinkedList<Rule> rules = new LinkedList<>();

    private Condition preCondition;
    private Action preAction;
    private Action postAction;
    private Condition stopCondition;

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

    public RuleSetBuilder order(RuleSet.ORDER order) {
        this.order = order;
        return this;
    }

    public RuleSetBuilder rule(Class<?> ruleClass) {
        rule(RuleBuilder.with(ruleClass).build());
        return this;
    }

    public RuleSetBuilder rule(ValidationRule rule) {
        rule(RuleBuilder.with(rule).build());
        return this;
    }

    public RuleSetBuilder rule(Rule rule) {
        Assert.notNull(rule, "Rule cannot be null.");
        this.rules.add(rule);
        return this;
    }

    public RuleSetBuilder rules(Rule...rules) {
        rules(Arrays.asList(rules));
        return this;
    }

    public RuleSetBuilder rules(Collection<Rule> rules) {
        Assert.notNull(rules, "Rules cannot be null.");
        this.rules.addAll(rules);
        return this;
    }

    /**
     * PreCondition(Optional) Condition to be met before the execution of the RuleSet.
     *
     * @param condition pre check before execution of the RuleSet.
     */
    RuleSetBuilder preCondition(Condition condition) {
        this.preCondition = preCondition;
        return this;
    }

    /**
     * PostAction(Optional) to be performed before the execution of the RuleSet.
     *
     * @param action pre action before the execution of the RuleSet.
     * @return this for fluency.
     */
    public RuleSetBuilder preAction(Action action) {
        this.preAction = action;
        return this;
    }

    /**
     * PreAction(Optional) to be performed after the execution of the RuleSet.
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

    public RuleSet build() {
        return new DefaultRuleSet(getName(), getDescription(), getOrder(), rules.toArray(new Rule[rules.size()]),
                getPreCondition(), getPreAction(), getPostAction(), getStopCondition());
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public RuleSet.ORDER getOrder() {
        return order;
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
}
