package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.impl.DefaultRuleEngine;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;

public interface RuleEngine {

    static RuleEngine defaultRuleEngine() {
        return new DefaultRuleEngine();
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    boolean isPass(Rule rule, RuleDefinition ruleDefinition, Object target, RuleExecutionContext ctx) throws UnrulyException;

    void run(Rule rule, RuleDefinition ruleDefinition, Object target, RuleExecutionContext ctx) throws UnrulyException;

    void run(Action action, ActionDefinition actionDefinition, Object target, RuleExecutionContext ctx) throws UnrulyException;
}
