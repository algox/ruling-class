/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.algorithmx.rulii.core.condition;

import org.algorithmx.rulii.bind.BindingDeclaration;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;

public interface SimpleCondition extends Runnable<Boolean> {

    @Override
    default Boolean run(RuleContext context) throws ConditionExecutionException {
        return isTrue(context);
    }

    @Override
    default Boolean run(Bindings bindings) throws ConditionExecutionException {
        return run(RuleContextBuilder.build(bindings != null ? bindings : Bindings.create()));
    }

    @Override
    default Boolean run(BindingDeclaration...params) throws ConditionExecutionException {
        Bindings bindings = params != null ? Bindings.create().bind(params) : Bindings.create();
        return run(RuleContextBuilder.build(bindings));
    }

    /**
     * Derives all the arguments and executes this Condition.
     *
     * @param context Rule Context.
     * @return result of the function.
     * @throws ConditionExecutionException thrown if there are any errors during the Condition execution.
     */
    boolean isTrue(RuleContext context) throws ConditionExecutionException;

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
