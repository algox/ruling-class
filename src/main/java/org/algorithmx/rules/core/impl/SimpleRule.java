package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.core.ObjectFactory;
import org.algorithmx.rules.core.ParameterResolver;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRule implements Rule {

    private final RuleDefinition ruleDefinition;
    private final ParameterResolver parameterResolver;
    private final BindableMethodExecutor methodExecutor;
    private final Object targetClassInstance;

    public SimpleRule(RuleDefinition ruleDefinition, ParameterResolver parameterResolver,
                      BindableMethodExecutor methodExecutor, ObjectFactory objectFactory) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.parameterResolver = parameterResolver != null ? parameterResolver : ParameterResolver.create();
        this.methodExecutor = methodExecutor != null ? methodExecutor : BindableMethodExecutor.create();
        this.targetClassInstance = ruleDefinition.isStatic() ? null : objectFactory.create(ruleDefinition.getRulingClass());
    }

    @Override
    public boolean test(RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = parameterResolver.resolve(ruleDefinition.getCondition(), ctx.getBindings(), ctx.getMatchingStrategy());
        return methodExecutor.execute(targetClassInstance, ruleDefinition.getCondition(), args);
    }

    @Override
    public boolean run(Object... args) throws UnrulyException {
        return methodExecutor.execute(targetClassInstance, ruleDefinition.getCondition(), args);
    }

    @Override
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
