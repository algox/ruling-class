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
import org.algorithmx.rules.core.ResultExtractor;
import org.algorithmx.rules.core.RuleAuditor;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.ActionExecution;
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
    public <T> T run(RuleContext ctx, ResultExtractor<T> extractor) throws UnrulyException {
        run(ctx);
        // Extract the result from the Bindings.
        return extractor.extract(ctx.getBindings());
    }

    @Override
    public void run(RuleContext ctx) throws UnrulyException {
        RuleAuditor auditor = ctx.getAuditor();
        Binding<Object>[] bindings = null;
        RuleExecution ruleExecution;
        boolean pass;

        // TODO : Execute stopWhen
        try {
            // Set the RuleContext in the ThreadLocal so it can be accessed during the execution.
            RuleContext.set(ctx);
            // Find all tne matching Bindings.
            // TODO : Throw BindingException?
            bindings = ctx.getParameterResolver().resolveAsBindings(
                    ruleDefinition.getCondition(), ctx.getBindings(), ctx.getMatchingStrategy());
            // Execute the Rule
            pass = isPass(getBindingValues(bindings));
            // Store the result of the Execution
            ruleExecution = new RuleExecution(getRuleDefinition(), pass, bindings);
            auditor.audit(ruleExecution);
        } catch (Exception e) {
            // Store the error
            auditor.audit(new RuleExecution(getRuleDefinition(), e, bindings));
            UnrulyException ex = new UnrulyException("Error trying to execute rule condition [" + getName() + "]", e);
            if (ctx.isAuditingEnabled()) ex.setExecutionStack(ctx.getAuditor().getAuditItems());
            throw ex;
        } finally {
            // Clear the ThreadLocal
            RuleContext.clear();
        }

        // The Condition passed
        if (pass) {
            // Execute any associated Actions.
            for (Action action : getActions()) {
                runAction(ctx, action, ruleExecution);
            }
        } else if (getOtherwiseAction() != null) {
            // Condition failed
            runAction(ctx, getOtherwiseAction(), ruleExecution);
        }
    }

    protected void runAction(RuleContext ctx, Action action, RuleExecution ruleExecution) {
        Binding<Object>[] actionBindings = null;

        try {
            // Set the RuleContext so it can be accessed during the execution.
            RuleContext.set(ctx);
            // Execute any associated Actions.
            actionBindings = ctx.getParameterResolver().resolveAsBindings(action.getActionDefinition().getAction(),
                    ctx.getBindings(), ctx.getMatchingStrategy());
            // Execute the Action
            action.execute(getBindingValues(actionBindings));
            // Store the action audit
            ruleExecution.add(new ActionExecution(action.getActionDefinition(), actionBindings));
        } catch (Exception e) {
            // Store the action audit
            ruleExecution.add(new ActionExecution(action.getActionDefinition(), e, actionBindings));
            // TODO : Need a proper Rule action name
            UnrulyException ex = new UnrulyException("Error trying to execute rule action", e);
            if (ctx.isAuditingEnabled()) ex.setExecutionStack(ctx.getAuditor().getAuditItems());
            throw ex;
        } finally {
            // Clear the ThreadLocal
            RuleContext.clear();
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
