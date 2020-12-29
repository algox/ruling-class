package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

public class ConditionExecutionException extends UnrulyException {

    private Condition condition;
    private ParameterMatch[] matches;
    private Object[] values;

    public ConditionExecutionException(String message, Throwable cause, Condition condition, ParameterMatch[] matches,
                                       Object[] values) {
        super(message + System.lineSeparator() + RuleUtils.getMethodDescription(condition.getMethodDefinition(),
                matches, values, ""), cause);
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
        this.matches = matches;
        this.values = values;
    }

    public Condition getCondition() {
        return condition;
    }

    public ParameterMatch[] getMatches() {
        return matches;
    }

    public Object[] getValues() {
        return values;
    }
}
