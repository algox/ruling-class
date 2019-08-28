package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;

public class DefaultRuleEngine implements RuleEngine {

    public DefaultRuleEngine() {
        super();
    }

    @Override
    public void run(Rule rule, RuleExecutionContext ctx) throws UnrulyException {
        Assert.notNull(rule, "rule cannot be null.");

        Object[] ruleArgs = resolveArguments(rule.getRuleDefinition().getCondition(), ctx.parameterResolver(),
                ctx.bindings(), ctx.matchingStrategy());

        if (rule.isPass(ruleArgs)) {
            for (Action action : rule.getActions()) {
                Object[] actionArgs = resolveArguments(action.getActionDefinition().getAction(),
                        ctx.parameterResolver(), ctx.bindings(), ctx.matchingStrategy());
                action.execute(actionArgs);
            }
            Arrays.stream(rule.getActions()).forEach(action -> action.execute(ctx));
        }
    }

    @Override
    public void run(RuleSet rules, RuleExecutionContext ctx) throws UnrulyException {
        Arrays.stream(rules.getRules()).forEach(rule -> run(rule, ctx));
    }

    protected Object[] resolveArguments(MethodDefinition methodDefinition, ParameterResolver parameterResolver,
                                        Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return parameterResolver.resolve(methodDefinition, bindings, matchingStrategy);
    }
}
