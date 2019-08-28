package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.ParameterResolver;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.MethodDefinition;
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
        Object[] args = resolveArguments(actionDefinition.getAction(), ctx.parameterResolver(),
                ctx.bindings(), ctx.matchingStrategy());
        ctx.methodExecutor().execute(target, actionDefinition.getAction(), args);
    }

    protected Object[] resolveArguments(MethodDefinition methodDefinition, ParameterResolver parameterResolver,
                                        Bindings bindings, BindingMatchingStrategy matchingStrategy) {
        return parameterResolver.resolve(methodDefinition, bindings, matchingStrategy);
    }
}