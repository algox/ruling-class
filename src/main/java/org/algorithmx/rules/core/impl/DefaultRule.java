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

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.model.RuleExecution;
import org.algorithmx.rules.spring.util.Assert;

/**
 * Default Rule Implementation (implements Identifiable).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultRule extends RuleTemplate implements Identifiable {

    private final BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();

    private final RuleDefinition ruleDefinition;
    private final Object target;

    public DefaultRule(RuleDefinition ruleDefinition, Object target) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
    }

    @Override
    public boolean isPass(Object...args) throws UnrulyException {
        return methodExecutor.execute(target, ruleDefinition.getCondition(), args);
    }

    @Override
    public void run(RuleContext ctx) throws UnrulyException {
        // Audit data
        RuleExecution audit = ctx.isAuditingEnabled() ? new RuleExecution(getRuleDefinition()) : null;

        try {
            // Set the RuleContext in the ThreadLocal so it can be accessed during the execution.
            RuleContext.set(ctx);
            // Find all tne matching Bindings.
            Binding<Object>[] bindings = ctx.getParameterResolver().resolveAsBindings(
                    ruleDefinition.getCondition(), ctx.getBindings(), ctx.getMatchingStrategy());
            // Audit the matched Bindings
            if (audit != null) audit.add(bindings);
            // Execute the Rule
            boolean result = isPass(getBindingValues(bindings));
            // Store the result of the Execution
            if (audit != null) audit.setPass(result);

            // The Condition passed
            if (result) {
                // Execute any associated Actions.
                for (Action action : getActions()) {
                    runAction(ctx, action, audit);
                }
            } else if (getOtherwiseAction() != null) {
                runAction(ctx, getOtherwiseAction(), audit);
            }

        } catch (Exception e) {
            // Store the error
            audit.setError(e);
            UnrulyException ex = new UnrulyException("Error trying to execute rule [" + getName() + "]", e);
            if (ctx.isAuditingEnabled()) ex.setExecutionStack(ctx.getAuditor().getAuditItems());
            throw ex;
        } finally {
            // Clear the ThreadLocal
            RuleContext.clear();
            if (ctx.isAuditingEnabled()) ctx.getAuditor().audit(audit);
        }
    }

    protected void runAction(RuleContext ctx, Action action, RuleExecution audit) {
        // Execute any associated Actions.
        Binding<Object>[] actionBindings = ctx.getParameterResolver().resolveAsBindings(action.getActionDefinition().getAction(),
                ctx.getBindings(), ctx.getMatchingStrategy());
        // Store the matched Bindings
        if (audit != null) audit.add(actionBindings);
        // Execute the Action
        action.execute(getBindingValues(actionBindings));
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

    private Object[] getBindingValues(Binding<Object>[] bindings) {
        if (bindings == null || bindings.length == 0) return null;

        Object[] result = new Object[bindings.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = bindings[i] == null ? null : bindings[i].getValue();
        }

        return result;
    }

    @Override
    public String toString() {
        return "DefaultRule{" +
                "ruleDefinition=" + ruleDefinition +
                '}';
    }
}
