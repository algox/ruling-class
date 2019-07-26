package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRule implements Rule {

    private final RuleDefinition ruleDefinition;
    private Object targetClassInstance;

    public SimpleRule(RuleDefinition ruleDefinition) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
    }

    @Override
    public boolean run(RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = ctx.parameterResolver().resolve(ruleDefinition.getCondition(),
                ctx.bindings(), ctx.matchingStrategy());
        return run(ctx, args);
    }

    @Override
    public boolean run(RuleExecutionContext ctx, Object... args) throws UnrulyException {
        return ctx.bindableMethodExecutor().execute(ruleDefinition.isStatic()
                        ? null
                        : getOrCreateRuleInstance(ctx.objectFactory(), ruleDefinition.getRulingClass()),
                ruleDefinition.getCondition(), args);
    }

    private Object getOrCreateRuleInstance(ObjectFactory objectFactory, Class<?> rulingClass) {
        if (this.targetClassInstance == null) {
            this.targetClassInstance = objectFactory.create(rulingClass);
        }
        return this.targetClassInstance;
    }
}
