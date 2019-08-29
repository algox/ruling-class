package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.*;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;

public class DefaultRuleEngine implements RuleEngine {

    public DefaultRuleEngine() {
        super();
    }

    @Override
    public void run(Rule rule, RuleExecutionContext ctx) throws UnrulyException {
        Assert.notNull(rule, "rule cannot be null.");

        RuleCommand command = new RuleCommand();
        command.execute(rule, ctx, ctx);
    }

    @Override
    public void run(RuleSet rules, RuleExecutionContext ctx) throws UnrulyException {
        Arrays.stream(rules.getRules()).forEach(rule -> run(rule, ctx));
    }
}
