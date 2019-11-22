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
package org.algorithmx.rules.core;

import org.algorithmx.rules.core.impl.RulingClass;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;
import org.algorithmx.rules.util.RuleUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Builder class used to build Lambda based Rules.
 *
 * @author Max Arulananthan.
 * @since 1.0
 */
public class RuleBuilder {

    private String name;
    private String description;
    private ConditionConsumer conditionConsumer;
    private List<Action> thenActions = new ArrayList<>();
    private Action otherwiseAction;

    RuleBuilder() {
        super();
    }

    RuleBuilder(String name) {
        super();
        name(name);
    }

    /**
     * Name of the Rule.
     *
     * @param name name of the Rule.
     * @return this for fluency.
     */
    public RuleBuilder name(String name) {
        Assert.isTrue(name != null && name.trim().length() > 0, "name cannot be null/empty.");
        this.name = name;
        return this;
    }

    /**
     * Rule description.
     *
     * @param description Rule description.
     * @return this for fluency.
     */
    public RuleBuilder description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Rule when condition (ie: @Given).
     *
     * @param condition Rule when condition.
     * @return this for fluency.
     */
    public RuleBuilder given(ConditionConsumer condition) {
        Assert.notNull(condition, "condition cannot be null.");
        this.conditionConsumer = condition;
        return this;
    }

    /**
     * Then action (ie: @Then).
     *
     * @param action Then action.
     * @return this for fluency.
     */
    public RuleBuilder then(ActionConsumer action) {
        Assert.notNull(action, "action cannot be null.");
        thenActions.add(ActionUtils.create(action, null, null));
        return this;
    }

    /**
     * Then action (ie: @Then).
     *
     * @param action Then Action.
     * @param description description of the action.
     * @return this for fluency.
     */
    public RuleBuilder then(ActionConsumer action, String description) {
        Assert.notNull(action, "action cannot be null.");
        thenActions.add(ActionUtils.create(action, description, null));
        return this;
    }

    /**
     * Else action (ie: @Otherwise).
     *
     * @param otherwise Else action.
     * @return this for fluency.
     */
    public RuleBuilder otherwise(ActionConsumer otherwise) {
        Assert.notNull(otherwise, "otherwise cannot be null.");
        otherwiseAction = ActionUtils.create(otherwise, null, null);
        return this;
    }

    /**
     * Else action (ie: @Otherwise).
     *
     * @param otherwise Else action.
     * @param description description of the the Else action.
     * @return this for fluency.
     */
    public RuleBuilder otherwise(ActionConsumer otherwise, String description) {
        Assert.notNull(otherwise, "otherwise cannot be null.");
        otherwiseAction = ActionUtils.create(otherwise, description, null);
        return this;
    }

    /**
     * Builds the Rule based on the given properties.
     *
     * @return new Rule.
     */
    public Rule build() {
        Assert.isTrue(name != null && name.trim().length() > 0, "name cannot be null/empty.");
        Assert.notNull(conditionConsumer, "condition cannot be null.");
        RuleDefinition ruleDefinition = RuleUtils.load(conditionConsumer, name, description);
        return new RulingClass(ruleDefinition, null, thenActions, otherwiseAction);
    }
}
