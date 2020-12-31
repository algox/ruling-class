package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

public class DefaultNotCondition implements NotCondition {

    private BasicCondition condition;

    public DefaultNotCondition(BasicCondition condition) {
        super();
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
    }

    public BasicCondition getCondition() {
        return condition;
    }

    @Override
    public boolean isPass(RuleContext ctx) throws ConditionExecutionException {
        return !condition.isPass(ctx);
    }
}
