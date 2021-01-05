package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Runnable;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.context.RuleContextBuilder;

public interface SimpleCondition extends Runnable<Boolean> {

    @Override
    default Boolean run(RuleContext context) {
        return isTrue(context);
    }

    /**
     * Derives all the arguments and executes this Condition.
     *
     * @param ctx Rule Context.
     * @return result of the function.
     * @throws ConditionExecutionException thrown if there are any errors during the Condition execution.
     */
    boolean isTrue(RuleContext ctx) throws ConditionExecutionException;

    /**
     * Derives all the arguments and executes this Condition.
     *
     * @param params Condition Parameters.
     * @return true if the Condition passed; false otherwise.
     * @throws ConditionExecutionException thrown if there are any errors during the Condition execution.
     */
    default boolean isTrue(BindingDeclaration...params) throws ConditionExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return isTrue(RuleContextBuilder.build(bindings));
    }

    default SimpleCondition not() {
        return new DefaultNotCondition(this);
    }

    default CompositeCondition and(SimpleCondition condition) {
        return new DefaultCompositeCondition(this, condition, (a, b) -> a && b);
    }

    default CompositeCondition or(SimpleCondition condition) {
        return new DefaultCompositeCondition(this, condition, (a, b) -> a || b);
    }

    default CompositeCondition xor(SimpleCondition condition) {
        return new DefaultCompositeCondition(this, condition, (a, b) -> a ^ b);
    }
}
