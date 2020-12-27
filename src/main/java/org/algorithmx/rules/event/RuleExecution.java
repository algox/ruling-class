package org.algorithmx.rules.event;

import org.algorithmx.rules.bind.match.ParameterMatch;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.Arrays;

public class RuleExecution<T> {

    private T result;
    private Rule rule;
    private ParameterMatch[] parameterMatches;
    private Object[] values;

    public RuleExecution(T result, Rule rule, ParameterMatch[] parameterMatches, Object[] values) {
        super();
        Assert.notNull(rule, "rule cannot be null.");
        this.result = result;
        this.rule = rule;
        this.parameterMatches = parameterMatches;
        this.values = values;
    }

    public T getResult() {
        return result;
    }

    public Rule getRule() {
        return rule;
    }

    public ParameterMatch[] getParameterMatches() {
        return parameterMatches;
    }

    public Object[] getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "RuleExecution{" +
                "result=" + result +
                ", rule=" + rule +
                ", parameterMatches=" + Arrays.toString(parameterMatches) +
                ", values=" + Arrays.toString(values) +
                '}';
    }
}
