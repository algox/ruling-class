package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.function.BiPredicate;

public class DefaultCompositeCondition implements CompositeCondition {

    private final SimpleCondition leftOperand;
    private final SimpleCondition rightOperand;
    private final BiPredicate<Boolean, Boolean> predicate;

    public DefaultCompositeCondition(SimpleCondition leftOperand, SimpleCondition rightOperand, BiPredicate<Boolean, Boolean> predicate) {
        super();
        Assert.notNull(leftOperand, "leftOperand cannot be null.");
        Assert.notNull(rightOperand, "rightOperand cannot be null.");
        Assert.notNull(predicate, "predicate cannot be null.");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.predicate = predicate;
    }

    @Override
    public SimpleCondition getLeftOperand() {
        return leftOperand;
    }

    @Override
    public SimpleCondition getRightOperand() {
        return rightOperand;
    }

    @Override
    public BiPredicate<Boolean, Boolean> getPredicate() {
        return predicate;
    }

    @Override
    public boolean isTrue(RuleContext ctx) throws ConditionExecutionException {
        boolean leftResult = leftOperand.isTrue(ctx);
        boolean rightResult = leftOperand.isTrue(ctx);
        return predicate.test(leftResult, rightResult);
    }
}
