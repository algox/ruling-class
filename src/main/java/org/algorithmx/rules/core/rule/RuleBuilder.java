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
import org.algorithmx.rules.util.reflect.ObjectFactory;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Builder class for creating Rule instances.
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public abstract class RuleBuilder {

    private String name;
    private String description;
    private Condition condition;
    private Action otherwiseAction;
    private Object target;
    private final List<Action> thenActions = new ArrayList<>();
    private ObjectFactory objectFactory = ObjectFactory.create();

    public static ClassBasedRuleBuilder with(Class<?> ruleClass) {
        return new ClassBasedRuleBuilder(ruleClass);
    }

    public static ObjectBasedRuleBuilder with(Object ruleTarget) {
        return new ObjectBasedRuleBuilder(ruleTarget);
    }

    public static ClassBasedRuleBuilder with(Class<?> ruleClass, ObjectFactory objectFactory) {
        ClassBasedRuleBuilder result = new ClassBasedRuleBuilder(ruleClass);
        result.objectFactory(objectFactory);
        return result;
    }

    public static LambdaBasedRuleBuilder with(Condition condition) {
        return new LambdaBasedRuleBuilder(condition);
    }

    protected RuleBuilder() {
        super();
    }

    /**
     * Name of the Rule.
     *
     * @param name name of the Rule.
     * @return LambdaBasedRuleBuilder for fluency.
     */
    public RuleBuilder name(String name) {
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

    public RuleBuilder given(Condition condition) {
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
        return this;
    }

    public RuleBuilder then(Action action) {
        Assert.notNull(action, "action cannot be null.");
        action.getActionDefinition().setOrder(thenActions.size());
        this.thenActions.add(action);
        return this;
    }

    public RuleBuilder otherwise(Action action) {
        this.otherwiseAction = action;
        return this;
    }

    public RuleBuilder clearActions() {
        this.thenActions.clear();
        return this;
    }

    protected RuleBuilder target(Object target) {
        this.target = target;
        return this;
    }

    public RuleBuilder objectFactory(ObjectFactory objectFactory) {
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
        return this;
    }

    public abstract Class<?> getRuleClass();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public Rule build() {
        // Sort Then Action per Order
        Collections.sort(thenActions);

        ActionDefinition[] actionDefinitions = new ActionDefinition[getThenActions().size()];

        for (int i = 0; i < actionDefinitions.length; i++) {
            actionDefinitions[i] = getThenActions().get(i).getActionDefinition();
        }

        RuleDefinition ruleDefinition = new RuleDefinition(getRuleClass(), getName(), getDescription(),
                getCondition().getConditionDefinition(), actionDefinitions,
                getOtherwiseAction() != null ? getOtherwiseAction().getActionDefinition() : null);

        // Call back to set the RuleDefinition
        if (getTarget() instanceof RuleDefinitionAware) {
            ((RuleDefinitionAware) getTarget()).setRuleDefinition(ruleDefinition);
        }

        return new RulingClass(ruleDefinition, getTarget(), getCondition(), getThenActions(), getOtherwiseAction());
    }
}
