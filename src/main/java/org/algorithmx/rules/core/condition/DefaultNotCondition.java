package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

public class DefaultNotCondition implements NotCondition {

    private SimpleCondition condition;

    public DefaultNotCondition(SimpleCondition condition) {
        super();
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
    }

    public SimpleCondition getCondition() {
        return condition;
    }

    @Override
    public boolean isPass(RuleContext ctx) throws ConditionExecutionException {
        return !condition.isPass(ctx);
    }
}
