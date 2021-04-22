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

package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.core.Runnable;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;

import java.lang.annotation.Annotation;

public interface AnnotatedRunnableBuilder<T extends Annotation> {

    Runnable build(T type, String bindingName);

    default Rule buildRule(BindingValidationRule target, String when) {

        if (when != null) {
            return RuleBuilder
                    .with(target)
                    .preCondition(ConditionBuilder.build(when))
                    .build();
        }

        return RuleBuilder.build(target);
    }
}
