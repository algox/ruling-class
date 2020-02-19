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

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ConditionUtils;

import java.util.List;

/**
 * Default Rule Implementation (implements Identifiable).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RulingClass extends RuleTemplate {

    private final RuleDefinition ruleDefinition;
    private final Object target;
    private final Condition condition;

    /**
     * Rule wrapped around a external target object (ie: Rule defined in an external class).
     *
     * @param ruleDefinition rule meta information.
     * @param target object where the Rule is actually defined.
     */
    public RulingClass(RuleDefinition ruleDefinition, Object target) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.condition = ConditionUtils.create(ruleDefinition.getConditionDefinition(), target);
        loadActions(ruleDefinition.getRuleClass());
    }

    /**
     * Rule that fully externally defined.
     *
     * @param ruleDefinition meta information.
     * @param target target Rule class.
     * @param thenActions all the Then actions (optional).
     * @param otherwiseAction the Otherwise action (optional);
     */
    public RulingClass(RuleDefinition ruleDefinition, Object target, List<Action> thenActions, Action otherwiseAction) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.condition = ConditionUtils.create(ruleDefinition.getConditionDefinition(), target);

        // Then actions (optional)
        if (thenActions != null) {
            thenActions.stream().forEach(action -> then(action));
        }

        // Otherwise action (Optional)
        if (otherwiseAction != null) {
            otherwise(otherwiseAction);
        }
    }

    /**
     * Rule defined with all the given properties.
     *
     * @param ruleDefinition meta information.
     * @param target target Rule class.
     * @param condition given condition.
     * @param thenActions all the Then actions.
     * @param otherwiseAction the Otherwise action (optional);
     */
    public RulingClass(RuleDefinition ruleDefinition, Object target, Condition condition, List<Action> thenActions, Action otherwiseAction) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.condition = condition;

        // Then actions (optional)
        if (thenActions != null) {
            thenActions.stream().forEach(action -> then(action));
        }

        // Otherwise action (Optional)
        if (otherwiseAction != null) {
            otherwise(otherwiseAction);
        }
    }

    /**
     * Ctor used to subclasses than are actually implementing the rule.
     */
    protected RulingClass() {
        super();
        this.ruleDefinition = RuleDefinition.load(getClass());
        this.target = this;
        this.condition = ConditionUtils.create(ruleDefinition.getConditionDefinition(), target);
        loadActions(getClass());
    }

    @Override
    public Condition getCondition() {
        return condition;
    }

    @Override
    public boolean isPass(Object...args) throws UnrulyException {
        return condition.isPass(args);
    }

    @Override
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public String getName() {
        return ruleDefinition.getName();
    }

    @Override
    public String getDescription() {
        return ruleDefinition.getDescription();
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public final boolean isIdentifiable() {
        return true;
    }

    @Override
    public String toString() {
        return "RulingClass{" +
                "ruleDefinition=" + ruleDefinition +
                '}';
    }
}
