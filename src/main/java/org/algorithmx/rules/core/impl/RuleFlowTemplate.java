package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.spring.util.Assert;

public abstract class RuleFlowTemplate<T> implements RuleFlow<T> {

    private final RuleExecutionContext ctx;

    public RuleFlowTemplate(RuleExecutionContext ctx) {
        super();
        Assert.notNull(ctx, "ctx cannot be null.");
        this.ctx = ctx;
    }

    @Override
    public final Bindings bindings() {
        return ctx.bindings();
    }

    /*@Override
    public final boolean rule(Rule rule) {
        Assert.notNull(rule, "rule cannot be null.");
        return rule.run(ctx);
    }

    @Override
    public final boolean and(Rule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        CompositeRule rule = RuleFactory.and(rules);
        return rule(rule);
    }

    @Override
    public final boolean or(Rule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        CompositeRule rule = RuleFactory.or(rules);
        return rule(rule);
    }

    @Override
    public final boolean none(Rule[] rules) {
        Assert.notNull(rules, "rules cannot be null.");
        CompositeRule rule = RuleFactory.none(rules);
        return rule(rule);
    }

    @Override
    public final boolean and(RuleSet ruleSet) {
        return and(ruleSet.getRules());
    }

    @Override
    public final boolean or(RuleSet ruleSet) {
        return or(ruleSet.getRules());
    }

    @Override
    public final boolean none(RuleSet ruleSet) {
        return none(ruleSet.getRules());
    }*/
}
