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
import org.algorithmx.rules.core.ResultExtractor;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ConditionUtils;

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

    public RulingClass(RuleDefinition ruleDefinition, Object target) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
        this.condition = ConditionUtils.create(ruleDefinition.getCondition(), target);
    }

    protected RulingClass() {
        super();
        this.ruleDefinition = RuleDefinition.load(getClass());
        this.target = this;
        this.condition = ConditionUtils.create(ruleDefinition.getCondition(), target);
        loadActions(getClass());
    }

    @Override
    public boolean isPass(Object...args) throws UnrulyException {
        return condition.isPass(args);
    }

    @Override
    public <T> T run(RuleContext ctx, ResultExtractor<T> extractor) throws UnrulyException {
        run(ctx);
        // Extract the result from the Bindings.
        return extractor.extract(ctx.getBindings());
    }

    @Override
    public void run(RuleContext ctx) throws UnrulyException {
        boolean result;

        // TODO : Execute stopWhen
        try {
            // Execute the Rule
            result = condition.isPass(ctx);
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute rule condition [" + getName() + "]", e);
            throw ex;
        }

        // The Condition passed
        if (result) {
            // Execute any associated Actions.
            for (Action action : getActions()) {
                runAction(ctx, action);
            }
        } else if (getOtherwiseAction() != null) {
            // Condition failed
            runAction(ctx, getOtherwiseAction());
        }
    }

    /**
     * Executes the Action associated with the Condition.
     *
     * @param ctx Rule Context.
     * @param action desired action to execute.
     */
    protected void runAction(RuleContext ctx, Action action) {

        try {
            // Execute the Action
            action.execute(ctx);
        } catch (Exception e) {
            UnrulyException ex = new UnrulyException("Error trying to execute rule action ["
                    + action.getActionDefinition().getActionName() + "]", e);
            throw ex;
        }
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
