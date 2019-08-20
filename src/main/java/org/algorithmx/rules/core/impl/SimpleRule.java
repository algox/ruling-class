package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;

import java.util.LinkedList;

public class SimpleRule implements Rule, Identifiable {

    private final RuleDefinition ruleDefinition;
    private final Object target;
    private final LinkedList<Action> actions = new LinkedList();

    public SimpleRule(RuleDefinition ruleDefinition, Object target) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
    }

    @Override
    public boolean isPass(RuleExecutionContext ctx) throws UnrulyException {
        Object[] args = ctx.parameterResolver().resolve(ruleDefinition.getCondition(),
                ctx.bindings(), ctx.matchingStrategy());
        return isPass(ctx, args);
    }

    protected boolean isPass(RuleExecutionContext ctx, Object... args) throws UnrulyException {
        return ctx.bindableMethodExecutor().execute(ruleDefinition.isStatic()
                        ? null
                        : target, ruleDefinition.getCondition(), args);
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public String getName() {
        return ruleDefinition.getName();
    }

    @Override
    public String getDescription() {
        return ruleDefinition.getDescription();
    }

    @Override
    public Object getTarget() {
        return target;
    }

    @Override
    public final boolean isIdentifiable() {
        return true;
    }

    @Override
    public Action[] getActions() {
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Rule then(Action action) {
        actions.add(action);
        return this;
    }

    @Override
    public Rule then(Then action) {
        return then(action, null);
    }

    @Override
    public Rule then(Then action, String description) {
        actions.add(ActionUtils.create(action, description, getTarget()));
        return this;
    }

    @Override
    public Rule then(Then.Then0 arg) {
        return then(arg, null);
    }

    @Override
    public <A> Rule then(Then.Then1<A> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B> Rule then(Then.Then2<A, B> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C> Rule then(Then.Then3<A, B, C> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D> Rule then(Then.Then4<A, B, C, D> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E> Rule then(Then.Then5<A, B, C, D, E> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F> Rule then(Then.Then6<A, B, C, D, E, F> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G> Rule then(Then.Then7<A, B, C, D, E, F, G> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H> Rule then(Then.Then8<A, B, C, D, E, F, G, H> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> Rule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> Rule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg) {
        return then(arg, null);
    }
}
