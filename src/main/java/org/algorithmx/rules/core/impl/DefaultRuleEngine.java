package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.core.RuleSet;

public class DefaultRuleEngine implements RuleEngine {

    public DefaultRuleEngine() {
        super();
    }

    @Override
    public void run(RuleExecutionContext ctx, RuleSet... rules) throws UnrulyException {

    }
}
