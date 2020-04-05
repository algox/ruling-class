/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.core.condition;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.model.ConditionDefinition;
import org.algorithmx.rules.spring.util.Assert;

/**
 * As the name suggests this Condition delegates most of the functionality to its target Condition. This class gives
 * the creator the ability to override the ConditionDefinition. This is useful for Functional Conditions where you
 * cannot override the definition.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DelegatingCondition implements Condition {

    private final Condition targetCondition;
    private final ConditionDefinition definition;

    public DelegatingCondition(Condition targetCondition, ConditionDefinition definition) {
        super();
        Assert.notNull(targetCondition, "targetCondition cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        this.targetCondition = targetCondition;
        this.definition = definition;
    }

    @Override
    public boolean isPass(Object...params) throws UnrulyException {
        return targetCondition.isPass(params);
    }

    @Override
    public ConditionDefinition getConditionDefinition() {
        return definition;
    }

    @Override
    public Object getTarget() {
        return targetCondition.getTarget();
    }
}
