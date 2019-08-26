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

    void run(RuleExecutionContext ctx, RuleSet...rules) throws UnrulyException;
}
