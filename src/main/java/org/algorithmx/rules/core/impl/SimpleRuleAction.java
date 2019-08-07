package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleAction;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRuleAction implements RuleAction {

    private final Rule rule;
    private final Action action;

    public SimpleRuleAction(Rule rule, Action action) {
        super();
        Assert.notNull(rule, "rule cannot be null.");
        Assert.notNull(action, "action cannot be null.");
        this.rule = rule;
        this.action = action;
    }

    @Override
    public Rule getRule() {
        return null;
    }

    @Override
    public Action getAction() {
        return null;
    }
}
