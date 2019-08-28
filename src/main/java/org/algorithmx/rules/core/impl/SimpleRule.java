package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class SimpleRule extends RuleTemplate implements Identifiable {

    private final RuleDefinition ruleDefinition;
    private final Object target;

    private BindableMethodExecutor methodExecutor = BindableMethodExecutor.defaultBindableMethodExecutor();

    public SimpleRule(RuleDefinition ruleDefinition, Object target) {
        super();
        Assert.notNull(ruleDefinition, "ruleDefinition cannot be null");
        this.ruleDefinition = ruleDefinition;
        this.target = target;
    }

    @Override
    public boolean isPass(Object...args) throws UnrulyException {
        return methodExecutor.execute(target, ruleDefinition.getCondition(), args);
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

    public void setMethodExecutor(BindableMethodExecutor methodExecutor) {
        Assert.notNull(methodExecutor, "methodExecutor cannot be null.");
        this.methodExecutor = methodExecutor;
    }
}
