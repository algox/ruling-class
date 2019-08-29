package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.RuleExecution;

import java.util.Arrays;

public class RuleCommand {

    private ParameterResolver parameterResolver = ParameterResolver.defaultParameterResolver();

    public RuleCommand() {
        super();
    }

    public void execute(Rule rule, RuleExecutionContext ctx, RuleExecutionAuditor auditor) throws UnrulyException {
        RuleExecution audit = new RuleExecution(rule.getRuleDefinition());

        try {
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
                Arrays.stream(rule.getActions()).forEach(action -> action.execute(ctx));
            }
        } catch (Exception e) {
            audit.setError(e);
            String ruleName = rule.isIdentifiable() ? ((Identifiable) rule).getName() : null;
            throw new UnrulyException("Error trying to execute rule [" + (ruleName != null ? ruleName : "n/a") + "]", e);
        } finally {
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
