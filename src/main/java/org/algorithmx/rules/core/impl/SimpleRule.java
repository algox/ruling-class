package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.IdentifiableRule;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRule implements IdentifiableRule {

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
        Object[] args = ctx.parameterResolver().resolve(ruleDefinition.getCondition(),
                ctx.bindings(), ctx.matchingStrategy());
        return isPass(ctx, args);
    }

    protected boolean isPass(RuleExecutionContext ctx, Object... args) throws UnrulyException {
        return ctx.bindableMethodExecutor().execute(ruleDefinition.isStatic()
                        ? null
                        : target, ruleDefinition.getCondition(), args);
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
}
