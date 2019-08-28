package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleAction implements Action {

    private final ActionDefinition actionDefinition;
    private final Object target;

    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();

    public SimpleAction(ActionDefinition actionDefinition, Object target) {
        super();
        Assert.notNull(actionDefinition, "actionDefinition cannot be null.");
        this.actionDefinition = actionDefinition;
        this.target = target;
    }

    @Override
    public void execute(Object... args) {
        methodExecutor.execute(target, actionDefinition.getAction(), args);
    }

    @Override
    public ActionDefinition getActionDefinition() {
        return actionDefinition;
    }

    @Override
    public Object getTarget() {
        return target;
    }

    public void setMethodExecutor(BindableMethodExecutor methodExecutor) {
        Assert.notNull(methodExecutor, "methodExecutor cannot be null.");
        this.methodExecutor = methodExecutor;
    }
}