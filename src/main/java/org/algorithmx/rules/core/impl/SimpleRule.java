package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;

import java.util.LinkedList;

public class SimpleRule implements IdentifiableRule, ActionableRule {

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

    @Override
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
    public ActionableRule then(Action action) {
        actions.add(action);
        return this;
    }

    @Override
    public ActionableRule then(Then action) {
        return then(action, null);
    }

    @Override
    public ActionableRule then(Then action, String description) {
        actions.add(ActionUtils.create(action, description, getTarget()));
        return this;
    }

    @Override
    public ActionableRule then(Then.Then0 arg) {
        return then(arg, null);
    }

    @Override
    public <A> ActionableRule then(Then.Then1<A> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B> ActionableRule then(Then.Then2<A, B> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C> ActionableRule then(Then.Then3<A, B, C> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D> ActionableRule then(Then.Then4<A, B, C, D> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E> ActionableRule then(Then.Then5<A, B, C, D, E> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F> ActionableRule then(Then.Then6<A, B, C, D, E, F> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G> ActionableRule then(Then.Then7<A, B, C, D, E, F, G> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H> ActionableRule then(Then.Then8<A, B, C, D, E, F, G, H> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I> ActionableRule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg) {
        return then(arg, null);
    }

    @Override
    public <A, B, C, D, E, F, G, H, I, J> ActionableRule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg) {
        return then(arg, null);
    }
}
