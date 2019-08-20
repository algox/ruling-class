package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.spring.util.Assert;

import java.util.function.BiPredicate;

public class DefaultCompositeRule implements CompositeRule {

    private final Rule[] rules;
    private final BiPredicate<Rule[], RuleExecutionContext> test;

    public DefaultCompositeRule(Rule[] rules, BiPredicate<Rule[], RuleExecutionContext> test) {
        super();
        Assert.notNull(rules, "rules cannot be null");
        Assert.notNull(test, "test cannot be null");
        Assert.isTrue(rules.length > 0, " there must at least one rule");
        this.rules = rules;
        this.test = test;
    }

    @Override
    public boolean isPass(RuleExecutionContext ctx) throws UnrulyException {
        return test.test(rules, ctx);
    }

    @Override
    public final Rule[] getRules() {
        return rules;
    }

    @Override
    public Object getTarget() {
        return this;
    }

    @Override
    public Action[] getActions() {
        return new Action[0];
    }

    @Override
    public Rule then(Action action) {
        return null;
    }

    @Override
    public Rule then(Then action) {
        return null;
    }

    @Override
    public Rule then(Then action, String description) {
        return null;
    }

    @Override
    public Rule then(Then.Then0 arg) {
        return null;
    }

    @Override
    public <A> Rule then(Then.Then1<A> arg) {
        return null;
    }

    @Override
    public <A, B> Rule then(Then.Then2<A, B> arg) {
        return null;
    }

    @Override
    public <A, B, C> Rule then(Then.Then3<A, B, C> arg) {
        return null;
    }

    @Override
    public <A, B, C, D> Rule then(Then.Then4<A, B, C, D> arg) {
        return null;
    }

    @Override
    public <A, B, C, D, E> Rule then(Then.Then5<A, B, C, D, E> arg) {
        return null;
    }

    @Override
    public <A, B, C, D, E, F> Rule then(Then.Then6<A, B, C, D, E, F> arg) {
        return null;
    }

    @Override
    public <A, B, C, D, E, F, G> Rule then(Then.Then7<A, B, C, D, E, F, G> arg) {
        return null;
    }

    @Override
    public <A, B, C, D, E, F, G, H> Rule then(Then.Then8<A, B, C, D, E, F, G, H> arg) {
        return null;
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> Rule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg) {
        return null;
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> Rule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg) {
        return null;
    }
}
