package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleAction;
import org.algorithmx.rules.core.Then;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;

import java.util.ArrayList;
import java.util.List;

public class SimpleRuleAction implements RuleAction {

    private final Rule rule;
    private final List<Action> actions = new ArrayList<>();

    public SimpleRuleAction(Rule rule, Action action) {
        super();
        Assert.notNull(rule, "rule cannot be null.");
        Assert.notNull(action, "action cannot be null.");
        this.rule = rule;
        this.actions.add(action);
    }

    @Override
    public final Rule getRule() {
        return rule;
    }

    @Override
    public final Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public RuleAction then(Then action, String description) {
        Assert.notNull(action, "action cannot be null.");
        actions.add(new SimpleAction(ActionUtils.load(action, description), getRule().getTarget()));
        return this;
    }
}
