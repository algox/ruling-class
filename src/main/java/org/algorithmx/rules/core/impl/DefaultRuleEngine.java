package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleExecutionContext;

public class DefaultRuleEngine implements RuleEngine {

    public DefaultRuleEngine() {
        super();
    }
    
    @Override
    public boolean isPass(Rule rule, RuleExecutionContext ctx) throws UnrulyException {
        return rule.isPass(ctx);
    }

    @Override
    public void run(Rule rule, RuleExecutionContext ctx) throws UnrulyException {
        rule.run(ctx);
    }

    public void run(Action action, RuleExecutionContext ctx) throws UnrulyException {
        action.run(ctx);
    }
}
