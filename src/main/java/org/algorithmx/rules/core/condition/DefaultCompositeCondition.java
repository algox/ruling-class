package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.function.BiPredicate;

public class DefaultCompositeCondition implements CompositeCondition {

    private final BasicCondition leftOperand;
    private final BasicCondition rightOperand;
    private final BiPredicate<Boolean, Boolean> predicate;

    public DefaultCompositeCondition(BasicCondition leftOperand, BasicCondition rightOperand, BiPredicate<Boolean, Boolean> predicate) {
        super();
        Assert.notNull(leftOperand, "leftOperand cannot be null.");
        Assert.notNull(rightOperand, "rightOperand cannot be null.");
        Assert.notNull(predicate, "predicate cannot be null.");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.predicate = predicate;
    }

    @Override
    public BasicCondition getLeftOperand() {
        return leftOperand;
    }

    @Override
    public BasicCondition getRightOperand() {
        return rightOperand;
    }

    @Override
    public BiPredicate<Boolean, Boolean> getPredicate() {
        return predicate;
    }

    @Override
    public boolean isPass(RuleContext ctx) throws ConditionExecutionException {
        boolean leftResult = leftOperand.isPass(ctx);
        boolean rightResult = leftOperand.isPass(ctx);
        return predicate.test(leftResult, rightResult);
    }
}
