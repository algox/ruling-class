package org.algorithmx.rules.core.function;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;

public class FunctionExecutionException extends UnrulyException {

    private Function function;
    private ParameterMatch[] matches;
    private Object[] values;

    public FunctionExecutionException(String message, Throwable cause, Function function, ParameterMatch[] matches,
                                      Object[] values) {
        super(message + System.lineSeparator() + RuleUtils.getMethodDescription(function.getMethodDefinition(),
                matches, values, ""), cause);
        Assert.notNull(function, "function cannot be null.");
        this.function = function;
        this.matches = matches;
        this.values = values;
    }

    public Function getFunction() {
        return function;
    }

    public ParameterMatch[] getMatches() {
        return matches;
    }

    public Object[] getValues() {
        return values;
    }
}
