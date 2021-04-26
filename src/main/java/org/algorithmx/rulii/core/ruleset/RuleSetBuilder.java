/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.core.ruleset;

import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.model.Definition;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class RuleSetBuilder {

    private String name;
    private String description;
    private final LinkedList<Runnable> ruleSetItems = new LinkedList<>();

    private Condition preCondition;
    private Condition stopCondition;

    protected RuleSetBuilder() {
        super();
    }

    private RuleSetBuilder(String name) {
        this(name, null);
    }

    private RuleSetBuilder(String name, String description) {
        super();
        name(name);
        description(description);
    }

    public static RuleSetBuilder with(String name) {
        return new RuleSetBuilder(name);
    }

    public static RuleSetBuilder with(String name, String description) {
        return new RuleSetBuilder(name, description);
    }

    public static RuleSetBuilder with(String name, RuleSet parent) {
        return new RuleSetBuilder(name);
    }

    public static RuleSetBuilder with(String name, String description, RuleSet parent) {
        return new RuleSetBuilder(name, description);
    }

    public RuleSetBuilder name(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "RuleSet name [" + name + "] not valid. It must conform to ["
                + RuleUtils.NAME_REGEX + "]");
        this.name = name;
        return this;
    }

    public RuleSetBuilder description(String description) {
        this.description = description;
        return this;
    }

    public int size() {
        return ruleSetItems.size();
    }

    protected void assignParent(RuleSet parent) {
        // Not possible to have a cyclical dependency as we are yet to create this ruleset
        preCondition(parent.getPreCondition());
        stopCondition(parent.getStopCondition());
        add(parent.getRuleSetItems());
    }

    protected LinkedList<Runnable> getRuleSetItems() {
        return ruleSetItems;
    }

    public RuleSetBuilder rule(int index, Rule rule) {
        Assert.notNull(rule, "rule cannot be null");
        add(index, rule);
        return this;
    }

    public RuleSetBuilder rule(Rule rule) {
        Assert.notNull(rule, "rule cannot be null");
        add(rule);
        return this;
    }

    public RuleSetBuilder action(Action action) {
        Assert.notNull(action, "action cannot be null");
        add(action);
        return this;
    }

    public RuleSetBuilder action(int index, Action action) {
        Assert.notNull(action, "action cannot be null");
        add(index, action);
        return this;
    }

    public RuleSetBuilder rules(int index, Rule...rules) {
        Assert.notNullArray(rules, "rules");
        return addAllInternal(index, Arrays.asList(rules));
    }

    public RuleSetBuilder rules(Rule...rules) {
        Assert.notNullArray(rules, "rules");
        return addAllInternal(Arrays.asList(rules));
    }

    public RuleSetBuilder rules(RuleSet rules) {
        Assert.notNull(rules, "rules");
        add(rules);
        return this;
    }

    public RuleSetBuilder rules(int index, RuleSet rules) {
        Assert.notNull(rules, "rules");
        add(index, rules);
        return this;
    }

    public RuleSetBuilder actions(Action...actions) {
        Assert.notNullArray(actions, "actions");
        return addAllInternal(Arrays.asList(actions));
    }

    public RuleSetBuilder actions(int index, Action...actions) {
        Assert.notNullArray(actions, "actions");
        return addAllInternal(index, Arrays.asList(actions));
    }

    public RuleSetBuilder add(Runnable runnable) {
        Assert.notNull(runnable, "runnable cannot be null");
        getRuleSetItems().add(runnable);
        return this;
    }

    public RuleSetBuilder add(int index, Runnable runnable) {
        Assert.notNull(runnable, "runnable cannot be null");
        getRuleSetItems().add(index, runnable);
        return this;
    }

    public RuleSetBuilder add(Runnable...runnables) {
        Assert.notNull(runnables, "runnables cannot be null");
        addAllInternal(Arrays.asList(runnables));
        return this;
    }

    public RuleSetBuilder add(Collection<? extends Runnable> runnables) {
        return addAllInternal(runnables);
    }

    public RuleSetBuilder add(int index, Collection<? extends Runnable> runnables) {
        return addAllInternal(index, runnables);
    }

    public RuleSetBuilder rules(Collection<? extends Rule> rules) {
        return addAllInternal(rules);
    }

    public RuleSetBuilder rules(int index, Collection<? extends Rule> rules) {
        return addAllInternal(index, rules);
    }

    public RuleSetBuilder actions(Collection<? extends Action> actions) {
        return addAllInternal(actions);
    }

    public RuleSetBuilder actions(int index, Collection<? extends Action> actions) {
        return addAllInternal(index, actions);
    }

    public RuleSetBuilder or(Collection<Rule> rules) {
        Assert.notNull(rules, "rules cannot be null");
        RuleSet orRules = RuleSetBuilder.with("orRules")
                .add(rules)
                .stopCondition(ConditionBuilder.with((RuleSetResult ruleSetResult) -> ruleSetResult.isAnyPass()).build())
                .build();
        rules(orRules);
        return this;
    }

    public RuleSetBuilder or(Rule...rules) {
        Assert.notNull(rules, "rules cannot be null");
        return or(Arrays.asList(rules));
    }

    public RuleSetBuilder and(Collection<Rule> rules) {
        Assert.notNull(rules, "rules cannot be null");
        RuleSet andRules = RuleSetBuilder.with("andRules")
                .add(rules)
                .stopCondition(ConditionBuilder.with((RuleSetResult ruleSetResult) -> ruleSetResult.isAnyFail()).build())
                .build();
        rules(andRules);
        return this;
    }

    public RuleSetBuilder and(Rule...rules) {
        Assert.notNull(rules, "rules cannot be null");
        return and(Arrays.asList(rules));
    }

    private RuleSetBuilder addAllInternal(Collection<? extends Runnable> runnables) {
        return addAllInternal(null, runnables);
    }

    private RuleSetBuilder addAllInternal(Integer index, Collection<? extends Runnable> runnables) {
        Assert.notNull(runnables, "runnables cannot be null.");

        // Make sure we dont add null values.
        for (Runnable runnable : runnables) {
           Assert.notNull(runnable, "RuleSet items cannot be null.");

           if (index == null) {
               getRuleSetItems().add(runnable);
           } else {
               getRuleSetItems().add(index, runnable);
           }
        }

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
     * Condition that determines when execution should stop.
     *
     * @param condition stopping condition.
     * @return this for fluency.
     */
    public RuleSetBuilder stopCondition(Condition condition) {
        this.stopCondition = condition;
        return this;
    }

    public RuleSetDefinition buildRuleSetDefinition() {
        List<Definition> definitions = new ArrayList<>(getRuleSetItems().size());

        getRuleSetItems().stream().forEach(r -> {
            if (r instanceof Rule) {
                definitions.add(((Rule) r).getRuleDefinition());
            } else if (r instanceof RuleSet) {
                definitions.add(((RuleSet) r).getRuleSetDefinition());
            } else if (r instanceof Action) {
                definitions.add(((Action) r).getMethodDefinition());
            }
        });

        RuleSetDefinition result = new RuleSetDefinition(getName(), getDescription(),
                getPreCondition() != null ? getPreCondition().getMethodDefinition() : null,
                getStopCondition() != null ? getStopCondition().getMethodDefinition() : null,
                definitions.toArray(new Definition[definitions.size()]));

        return result;
    }

    public RuleSetBuilder clear() {
        this.ruleSetItems.clear();
        return this;
    }

    public RuleSet build() {
        return new RulingFamily(buildRuleSetDefinition(),
                getPreCondition(), getStopCondition(),
                getRuleSetItems().toArray(new Runnable[getRuleSetItems().size()]));
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

    public Condition getStopCondition() {
        return stopCondition;
    }

    @Override
    public String toString() {
        return "RuleSetBuilder{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ruleSetItems=" + ruleSetItems +
                ", preCondition=" + preCondition +
                ", stopCondition=" + stopCondition +
                '}';
    }
}
