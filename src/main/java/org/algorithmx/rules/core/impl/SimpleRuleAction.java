package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRuleAction implements RuleAction {

    private final IdentifiableRule condition;
    private final RuleDefinition ruleDefinition;
    private final ActionDefinition actionDefinition;
    private Object targetClassInstance;

    public SimpleRuleAction(RuleDefinition ruleDefinition, ActionDefinition actionDefinition) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null.");
        Assert.notNull(actionDefinition, "actionDefinition cannot be null.");
        this.condition = RuleFactory.rule(ruleDefinition);
        this.ruleDefinition = ruleDefinition;
        this.actionDefinition = actionDefinition;
    }

    @Override
    public void run(RuleExecutionContext ctx) throws UnrulyException {
        if (condition.isPass(ctx)) {
            Object[] args = ctx.parameterResolver().resolve(actionDefinition.getAction(),
                    ctx.bindings(), ctx.matchingStrategy());
            run(ctx, args);
        }
    }

    protected void run(RuleExecutionContext ctx, Object... args) throws UnrulyException {
        ctx.bindableMethodExecutor().execute(actionDefinition.isStatic()
                        ? null
                        : getActionInstance(ctx.objectFactory(), actionDefinition.getActionClass()),
                actionDefinition.getAction(), args);
    }

    private Object getActionInstance(ObjectFactory objectFactory, Class<?> actionClass) {
        if (this.targetClassInstance == null) this.targetClassInstance = objectFactory.create(actionClass);
        return this.targetClassInstance;
    }
}

