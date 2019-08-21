package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.impl.DefaultRuleEngine;

public interface RuleEngine {

    static RuleEngine defaultRuleEngine() {
        return new DefaultRuleEngine();
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    boolean isPass(Rule rule, RuleExecutionContext ctx) throws UnrulyException;

    void run(Rule rule, RuleExecutionContext ctx) throws UnrulyException;

    void run(Action action, RuleExecutionContext ctx) throws UnrulyException;
}
