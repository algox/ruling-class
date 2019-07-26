package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.impl.SimpleRule;
import org.algorithmx.rules.model.RuleDefinition;

public interface Rule extends BaseRule {

    static Rule create(RuleDefinition ruleDefinition) {
        return new SimpleRule(ruleDefinition);
    }

    default boolean run(Object... args) throws UnrulyException {
        return run(RuleExecutionContext.create(), args);
    }

    boolean run(RuleExecutionContext ctx, Object... args) throws UnrulyException;
}
