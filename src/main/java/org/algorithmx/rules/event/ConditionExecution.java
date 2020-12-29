package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class ConditionExecution {

    private final Condition condition;
    private final Boolean result;
    private final Exception error;
    private final MethodDefinition methodDefinition;
    private final ParameterMatch[] parameterMatches;
    private final Object[] values;

    public ConditionExecution(Condition condition, boolean result, MethodDefinition methodDefinition,
                              ParameterMatch[] parameterMatches, Object[] values) {
        this(condition, result, null, methodDefinition, parameterMatches, values);
    }

    public ConditionExecution(Condition condition, Exception error, MethodDefinition methodDefinition,
                              ParameterMatch[] parameterMatches, Object[] values) {
        this(condition, null, error, methodDefinition, parameterMatches, values);
    }

    private ConditionExecution(Condition condition, Boolean result, Exception error, MethodDefinition methodDefinition,
                               ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
        this.result = result;
        this.error = error;
        this.methodDefinition = methodDefinition;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public Condition getCondition() {
        return condition;
    }

    public Boolean getResult() {
        return result;
    }

    public boolean isError() {
        return error != null;
    }

    public boolean isSuccess() {
        return error == null;
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

    @Override
    public String toString() {
        return "ConditionExecution{" +
                "condition=" + condition +
                ", result=" + result +
                ", methodDefinition=" + methodDefinition +
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
