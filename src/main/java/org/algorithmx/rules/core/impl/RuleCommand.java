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
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.RuleExecution;

public class RuleCommand {

    private ParameterResolver parameterResolver = ParameterResolver.defaultParameterResolver();

    public RuleCommand() {
        super();
    }

    public void execute(Rule rule, RuleExecutionContext ctx, RuleExecutionAuditor auditor) throws UnrulyException {
        RuleExecution audit = new RuleExecution(rule.getRuleDefinition());

        try {
            RuleExecutionContext.set(ctx);
            Binding<Object>[] bindings = resolveArguments(rule.getRuleDefinition().getCondition(), parameterResolver,
                    ctx.bindings(), ctx.matchingStrategy());
            boolean result = rule.isPass(getBindingValues(bindings));

            audit.add(bindings);
            audit.setResult(result);

            if (result) {
                for (Action action : rule.getActions()) {
                    Binding<Object>[] actionBindings = resolveArguments(action.getActionDefinition().getAction(),
                            parameterResolver, ctx.bindings(), ctx.matchingStrategy());
                    audit.add(actionBindings);
                    action.execute(getBindingValues(actionBindings));
                }
            }
        } catch (Exception e) {
            audit.setError(e);
            String ruleName = rule.isIdentifiable() ? ((Identifiable) rule).getName() : null;
            UnrulyException ex = new UnrulyException("Error trying to execute rule [" + (ruleName != null ? ruleName : "n/a") + "]", e);
            ex.setExecutionStack(ctx.getAuditItems());
            throw ex;
        } finally {
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
