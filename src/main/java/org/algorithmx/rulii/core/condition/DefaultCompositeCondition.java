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

import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;

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
    public boolean isTrue(RuleContext context) throws ConditionExecutionException {
        boolean leftResult = leftOperand.isTrue(context);
        boolean rightResult = leftOperand.isTrue(context);
        return predicate.test(leftResult, rightResult);
    }
}
