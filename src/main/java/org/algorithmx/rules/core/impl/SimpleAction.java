package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleAction implements Action {

    private final ActionDefinition actionDefinition;
    private final Object target;

    public SimpleAction(ActionDefinition actionDefinition, Object target) {
        super();
        Assert.notNull(actionDefinition, "actionDefinition cannot be null.");
        this.actionDefinition = actionDefinition;
        this.target = target;
    }

    @Override
    public void run(RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = ctx.parameterResolver().resolve(actionDefinition.getAction(),
                ctx.bindings(), ctx.matchingStrategy());
        ctx.bindableMethodExecutor().execute(actionDefinition.isStatic()
                        ? null
                        : target, actionDefinition.getAction(), args);

    }
}