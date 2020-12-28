package org.algorithmx.rules.event;

import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleExecution<T> {

    private final Rule rule;
    private final T executingElement;

    public RuleExecution(Rule rule, T executingElement) {
        super();
        Assert.notNull(rule, "rule cannot be null.");
        this.rule = rule;
        this.executingElement = executingElement;
    }

    public T getExecutingElement() {
        return executingElement;
    }

    public Rule getRule() {
        return rule;
    }

    @Override
    public String toString() {
        return "RuleExecution{" +
                "rule=" + rule +
                ", executingElement=" + executingElement +
                '}';
    }
}
