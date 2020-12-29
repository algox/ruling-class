package org.algorithmx.rules.core.action;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

public class ActionExecutionException extends UnrulyException {

    private Action action;
    private ParameterMatch[] matches;
    private Object[] values;

    public ActionExecutionException(String message, Throwable cause, Action action, ParameterMatch[] matches,
                                    Object[] values) {
        super(message + System.lineSeparator() + RuleUtils.getMethodDescription(action.getMethodDefinition(),
                matches, values, ""), cause);
        Assert.notNull(action, "action cannot be null.");
        this.action = action;
        this.matches = matches;
        this.values = values;
    }

    public Action getAction() {
        return action;
    }

    public ParameterMatch[] getMatches() {
        return matches;
    }

    public Object[] getValues() {
        return values;
    }
}
