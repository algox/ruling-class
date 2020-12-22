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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;
import org.algorithmx.rules.util.reflect.ObjectFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Builder class for creating Rule instances.
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public abstract class RuleBuilder {

    private Class<?> ruleClass;
    private String name;
    private String description;
    private Condition preCondition = null;
    private Condition condition;
    private Action otherwiseAction;
    private Object target;
    private final List<Action> thenActions = new ArrayList<>();
    private RuleDefinition ruleDefinition = null;

    public static ClassBasedRuleBuilder with(Class<?> ruleClass) {
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        return with(ruleClass, ObjectFactory.create());
    }

    public static Rule create(Class<?> ruleClass) {
        return with(ruleClass).build();
    }

    public static ClassBasedRuleBuilder with(Object ruleTarget) {
        Assert.notNull(ruleTarget, "ruleTargetCannot be null");
        return ClassBasedRuleBuilder.with(ruleTarget.getClass(), ruleTarget);
    }

    public static Rule create(Object ruleTarget) {
        return with(ruleTarget).build();
    }

    public static ClassBasedRuleBuilder with(Class<?> ruleClass, ObjectFactory objectFactory) {
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        return ClassBasedRuleBuilder.with(ruleClass, objectFactory.createRule(ruleClass));
    }

    public static Rule create(Class<?> ruleClass, ObjectFactory objectFactory) {
        return with(ruleClass, objectFactory).build();
    }

    public static LambdaBasedRuleBuilder with(Condition condition) {
        Assert.notNull(condition, "condition cannot be null.");
        return new LambdaBasedRuleBuilder(condition);
    }

    public static Rule create(Condition condition, Action...actions) {
        LambdaBasedRuleBuilder builder = with(condition);
        if (actions != null) Arrays.stream(actions).forEach(a -> builder.then(a));
        return builder.build();
    }

    protected RuleBuilder() {
        super();
    }

    protected RuleBuilder ruleClass(Class<?> ruleClass) {
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        this.ruleClass = ruleClass;
        return this;
    }

    /**
     * Name of the Rule.
     *
     * @param name name of the Rule.
     * @return LambdaBasedRuleBuilder for fluency.
     */
    public RuleBuilder name(String name) {
        Assert.isTrue(RuleUtils.isValidName(name), "Rule name not valid. It must conform to [" + RuleUtils.NAME_REGEX + "]");
        this.name = name;
        return this;
    }

    /**
     * Rule description.
     *
     * @param description Rule description.
     * @return LambdaBasedRuleBuilder for fluency.
     */
    public RuleBuilder description(String description) {
        this.description = description;
        return this;
    }

    public RuleBuilder preCondition(Condition preCondition) {
        this.preCondition = preCondition;
        return this;
    }

    public RuleBuilder given(Condition condition) {
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
        return this;
    }

    public RuleBuilder then(Action action) {
        Assert.notNull(action, "action cannot be null.");
        // TODO : Do we need this?
        //action.getActionDefinition().setOrder(thenActions.size());
        this.thenActions.add(action);
        return this;
    }

    public RuleBuilder otherwise(Action action) {
        this.otherwiseAction = action;
        return this;
    }

    protected RuleBuilder target(Object target) {
        this.target = target;
        return this;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    public Class<?> getRuleClass() {
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

    public Object getTarget() {
        return target;
    }

    public Rule build() {
        Assert.notNull(getName(), "Rule Name cannot be null");

        // Sort Then Action per Order
        Collections.sort(thenActions);

        List<MethodDefinition> thenActionDefinitions = new ArrayList(thenActions.size());

        for (Action thenAction : thenActions) {
            thenActionDefinitions.add(thenAction.getMethodDefinition());
        }

        RuleDefinition ruleDefinition = new RuleDefinition(getRuleClass(), getName(), getDescription(),
                getPreCondition() != null ? getPreCondition().getMethodDefinition() : null,
                getCondition().getMethodDefinition(),
                thenActionDefinitions.toArray(new MethodDefinition[thenActionDefinitions.size()]),
                getOtherwiseAction() != null ? getOtherwiseAction().getMethodDefinition() : null);

        // Call back to set the RuleDefinition
        if (getTarget() instanceof RuleDefinitionAware) {
            ((RuleDefinitionAware) getTarget()).setRuleDefinition(ruleDefinition);
        }

        return new RulingClass(ruleDefinition, getTarget(), getPreCondition(), getCondition(),
                getThenActions(), getOtherwiseAction());
    }
}
