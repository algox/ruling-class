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
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.ParameterResolver;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleExecutionAuditor;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.RuleExecution;

/**
 * Command to execute the given rule and audit the results of the execution.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
class RuleCommand {

    private ParameterResolver parameterResolver = ParameterResolver.defaultParameterResolver();

    RuleCommand() {
        super();
    }

    /**
     * Execute the Rule and audits the results.
     *
     * @param rule rule to be executed.
     * @param ctx state management.
     * @param auditor auditor to keep track of the execution.
     * @throws UnrulyException thrown if there are any runtime exceptions during the execution.
     */
    void execute(Rule rule, RuleExecutionContext ctx, RuleExecutionAuditor auditor) throws UnrulyException {
        // Audit data
        RuleExecution audit = new RuleExecution(rule.getRuleDefinition());

        try {
            // Set the RuleExecutionContext in the ThreadLocal so it can be accessed during the execution.
            RuleExecutionContext.set(ctx);
            // Find all tne matching Bindings.
            Binding<Object>[] bindings = resolveArguments(rule.getRuleDefinition().getCondition(), parameterResolver,
                    ctx.bindings(), ctx.matchingStrategy());
            // Store the matched Bindings
            audit.add(bindings);
            // Execute the Rule
            boolean result = rule.isPass(getBindingValues(bindings));
            // Store the result of the Execution
            audit.setResult(result);
            // The Condition passed
            if (result) {
                // Execute any associated Actions.
                for (Action action : rule.getActions()) {
                    Binding<Object>[] actionBindings = resolveArguments(action.getActionDefinition().getAction(),
                            parameterResolver, ctx.bindings(), ctx.matchingStrategy());
                    // Store the matched Bindings
                    audit.add(actionBindings);
                    // Execute the Action
                    action.execute(getBindingValues(actionBindings));
                }
            }
        } catch (Exception e) {
            // Store the error
            audit.setError(e);
            String ruleName = rule.isIdentifiable() ? ((Identifiable) rule).getName() : null;
            UnrulyException ex = new UnrulyException("Error trying to execute rule [" + (ruleName != null ? ruleName : "n/a") + "]", e);
            ex.setExecutionStack(ctx.getAuditItems());
            throw ex;
        } finally {
            // Clear the ThreadLocal
            RuleExecutionContext.clear();
            auditor.audit(audit);
        }
    }

    protected Binding<Object>[] resolveArguments(MethodDefinition methodDefinition, ParameterResolver parameterResolver,
                                                 Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return parameterResolver.resolveAsBindings(methodDefinition, bindings, matchingStrategy);
    }

    private Object[] getBindingValues(Binding<Object>[] bindings) {
        if (bindings == null || bindings.length == 0) return null;

        Object[] result = new Object[bindings.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = bindings[i] != null ? bindings[i].getValue() : null;
        }

        return result;
    }
}
