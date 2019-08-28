package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.ParameterResolver;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRule extends RuleTemplate implements Identifiable {

    private final RuleDefinition ruleDefinition;
    private final Object target;

    public SimpleRule(RuleDefinition ruleDefinition, Object target) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
    }

    @Override
    public boolean isPass(RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = resolveArguments(ruleDefinition.getCondition(), ctx.parameterResolver(),
                ctx.bindings(), ctx.matchingStrategy());
        return ctx.methodExecutor().execute(target, ruleDefinition.getCondition(), args);
    }

    protected Object[] resolveArguments(MethodDefinition methodDefinition, ParameterResolver parameterResolver,
                                        Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return parameterResolver.resolve(methodDefinition, bindings, matchingStrategy);
    }

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
}
