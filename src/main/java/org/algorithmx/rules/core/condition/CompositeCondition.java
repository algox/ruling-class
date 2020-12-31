package org.algorithmx.rules.core.condition;

import java.util.function.BiPredicate;

public interface CompositeCondition extends SimpleCondition {

    SimpleCondition getLeftOperand();

    SimpleCondition getRightOperand();

    BiPredicate<Boolean, Boolean> getPredicate();
}
