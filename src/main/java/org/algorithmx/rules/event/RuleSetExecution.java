package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.ruleset.RuleSet;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class RuleSetExecution<T> {

    private T result;
    private RuleSet rules;
    private final MethodDefinition methodDefinition;
    private ParameterMatch[] parameterMatches;
    private Object[] values;

    public RuleSetExecution(T result, RuleSet rules, MethodDefinition methodDefinition,
                            ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(rules, "rules cannot be null.");
        this.result = result;
        this.rules = rules;
        this.methodDefinition = methodDefinition;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public T getResult() {
        return result;
    }

    public RuleSet getRules() {
        return rules;
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
        return "RuleSetExecution{" +
                "result=" + result +
                ", rules=" + rules +
                ", methodDefinition=" + methodDefinition +
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
