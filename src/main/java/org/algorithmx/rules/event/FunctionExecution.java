package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class FunctionExecution<T> {

    private final Function<T> function;
    private final T result;
    private final Exception error;
    private final MethodDefinition methodDefinition;
    private final ParameterMatch[] parameterMatches;
    private final Object[] values;

    public FunctionExecution(Function<T> function, T result, MethodDefinition methodDefinition,
                             ParameterMatch[] parameterMatches, Object[] values) {
        this(function, result, null, methodDefinition, parameterMatches, values);
    }

    public FunctionExecution(Function<T> function, Exception error, MethodDefinition methodDefinition,
                             ParameterMatch[] parameterMatches, Object[] values) {
        this(function, null, error, methodDefinition, parameterMatches, values);
    }

    private FunctionExecution(Function<T> function, T result, Exception error, MethodDefinition methodDefinition,
                             ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(function, "function cannot be null.");
        this.function = function;
        this.result = result;
        this.error = error;
        this.methodDefinition = methodDefinition;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public Function<T> getFunction() {
        return function;
    }

    public T getResult() {
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
        return "FunctionExecution{" +
                "function=" + function +
                ", result=" + result +
                ", methodDefinition=" + methodDefinition +
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
