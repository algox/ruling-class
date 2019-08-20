package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.RuleDefinition;

public class DefaultRuleEngine implements RuleEngine {

    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();

    public DefaultRuleEngine() {
        super();
    }

    /**
     * Executes the BindableMethod and returns its result. Following the steps below:
     *
     * First resolve all the parameters (as per the MethodDefinition) and Bind values to them using the given matching strategy.
     * Validate and make sure all required params are met.
     * Execute the BindableMethod with the derived args from above.
     */
    @Override
    public boolean isPass(Rule rule, RuleDefinition ruleDefinition, Object target, RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = resolveArguments(ruleDefinition.getCondition(), ctx.parameterResolver(), ctx.bindings(), ctx.matchingStrategy());
        return methodExecutor.execute(target, ruleDefinition.getCondition(), args);
    }

    @Override
    public void run(Rule rule, RuleDefinition ruleDefinition, Object target, RuleExecutionContext ctx) throws UnrulyException {
        if (isPass(rule, ruleDefinition, target, ctx)) {
            for (Action action : rule.getActions()) {
                action.run(ctx);
            }
        }
    }

    @Override
    public void run(Action action, ActionDefinition actionDefinition, Object target, RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = resolveArguments(actionDefinition.getAction(), ctx.parameterResolver(), ctx.bindings(), ctx.matchingStrategy());
        methodExecutor.execute(target, actionDefinition.getAction(), args);
    }

    protected Object[] resolveArguments(MethodDefinition methodDefinition, ParameterResolver parameterResolver,
                                        Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return parameterResolver.resolve(methodDefinition, bindings, matchingStrategy);
    }

    public BindableMethodExecutor getMethodExecutor() {
        return methodExecutor;
    }

    public void setMethodExecutor(BindableMethodExecutor methodExecutor) {
        this.methodExecutor = methodExecutor;
    }
}