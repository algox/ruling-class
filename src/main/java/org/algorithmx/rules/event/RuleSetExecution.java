package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.ruleset.RuleSet;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class RuleSetExecution<T> {

    private T result;
    private RuleSet rules;
    private ParameterMatch[] parameterMatches;
    private Object[] values;

    public RuleSetExecution(T result, RuleSet rules, ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(rules, "rules cannot be null.");
        this.result = result;
        this.rules = rules;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public T getResult() {
        return result;
    }

    public RuleSet getRules() {
        return rules;
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
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
