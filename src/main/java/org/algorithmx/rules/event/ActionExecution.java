package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.action.Action;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;

public class ActionExecution {

    private final Action action;
    private final Exception error;
    private final MethodDefinition methodDefinition;
    private final ParameterMatch[] parameterMatches;
    private final Object[] values;

    public ActionExecution(Action action, MethodDefinition methodDefinition,
                           ParameterMatch[] parameterMatches, Object[] values) {
        this(action, null, methodDefinition, parameterMatches, values);
    }

    public ActionExecution(Action action, Exception error, MethodDefinition methodDefinition,
                           ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(action, "action cannot be null.");
        this.action = action;
        this.error = error;
        this.methodDefinition = methodDefinition;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public Action getAction() {
        return action;
    }

    public boolean isError() {
        return error != null;
    }

    public MethodDefinition getMethodDefinition() {
        return methodDefinition;
    }

    public ParameterMatch[] getParameterMatches() {
        return parameterMatches;
    }

    public Object[] getValues() {
        return values;
    }
}
