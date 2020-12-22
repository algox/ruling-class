package org.algorithmx.rules.core.ruleset;

import org.algorithmx.rules.core.rule.RuleContext;

@FunctionalInterface
public interface RuleSetErrorHandler {

    boolean handle(Exception error, RuleContext context);
}
