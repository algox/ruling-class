package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;

public interface Rule extends IdentifiableRule {

    default boolean run(Object... args) throws UnrulyException {
        return run(RuleExecutionContext.create(), args);
    }

    boolean run(RuleExecutionContext ctx, Object... args) throws UnrulyException;
}
