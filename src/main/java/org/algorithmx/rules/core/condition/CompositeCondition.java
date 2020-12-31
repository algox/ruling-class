package org.algorithmx.rules.core.condition;

import java.util.function.BiPredicate;

public interface CompositeCondition extends BasicCondition {

    BasicCondition getLeftOperand();

    BasicCondition getRightOperand();

    BiPredicate<Boolean, Boolean> getPredicate();
}
