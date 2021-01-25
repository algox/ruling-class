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

package org.algorithmx.rulii.core.rule;

import org.algorithmx.rulii.core.action.Action;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.model.MethodDefinition;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AbstractRuleBuilder<T> {

    private Class<T> ruleClass;
    private String name;
    private String description;
    private Condition preCondition = null;
    private Condition condition;
    private Action otherwiseAction;
    private T target;
    private final List<Action> thenActions = new ArrayList<>();
    private RuleDefinition ruleDefinition = null;

    protected AbstractRuleBuilder() {
        super();
    }

    protected AbstractRuleBuilder<T> ruleClass(Class<T> ruleClass) {
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        this.ruleClass = ruleClass;
        return this;
    }

    /**
     * Name of the Rule.
     *
     * @param name name of the Rule.
     * @return this for fluency.
     */
    public AbstractRuleBuilder<T> name(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "Rule name [" + name + "] not valid. It must conform to ["
                + RuleUtils.NAME_REGEX + "]");
        this.name = name;
        return this;
    }

    /**
     * Rule description.
     *
     * @param description Rule description.
     * @return this for fluency.
     */
    public AbstractRuleBuilder<T> description(String description) {
        this.description = description;
        return this;
    }

    public AbstractRuleBuilder<T> preCondition(Condition preCondition) {
        this.preCondition = preCondition;
        return this;
    }

    public AbstractRuleBuilder<T> given(Condition condition) {
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
        return this;
    }

    public AbstractRuleBuilder<T> then(Action action) {
        Assert.notNull(action, "action cannot be null.");
        this.thenActions.add(action);
        return this;
    }

    public AbstractRuleBuilder<T> otherwise(Action action) {
        this.otherwiseAction = action;
        return this;
    }

    protected AbstractRuleBuilder<T> target(T target) {
        this.target = target;
        return this;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    public Class<T> getRuleClass() {
        return ruleClass;
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

    public Condition getCondition() {
        return condition;
    }

    public List<Action> getThenActions() {
        return thenActions;
    }

    public Action getOtherwiseAction() {
        return otherwiseAction;
    }

    public T getTarget() {
        return target;
    }

    public RuleDefinition buildRuleDefinition() {
        Assert.notNull(getName(), "Rule Name cannot be null");

        // Sort Then Action per Order
        Collections.sort(thenActions);

        List<MethodDefinition> thenActionDefinitions = new ArrayList(thenActions.size());

        for (Action thenAction : thenActions) {
            thenActionDefinitions.add(thenAction.getMethodDefinition());
        }

        return new RuleDefinition(getRuleClass(), getName(), getDescription(),
                getPreCondition() != null ? getPreCondition().getMethodDefinition() : null,
                getCondition().getMethodDefinition(),
                thenActionDefinitions.toArray(new MethodDefinition[thenActionDefinitions.size()]),
                getOtherwiseAction() != null ? getOtherwiseAction().getMethodDefinition() : null);
    }

    public void validate() {
        Assert.notNull(getCondition(), "Rule must have a Condition.");
    }

    public Rule<T> build() {
        validate();
        RuleDefinition ruleDefinition = buildRuleDefinition();

        // Call back to set the RuleDefinition
        if (getTarget() instanceof RuleDefinitionAware) {
            ((RuleDefinitionAware) getTarget()).setRuleDefinition(ruleDefinition);
        }

        return new RulingClass(ruleDefinition, getTarget(), getPreCondition(), getCondition(),
                getThenActions(), getOtherwiseAction());
    }
}
