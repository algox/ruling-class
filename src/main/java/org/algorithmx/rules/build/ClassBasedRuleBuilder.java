/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
package org.algorithmx.rules.build;

import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.ActionUtils;
import org.algorithmx.rules.util.ConditionUtils;

import java.util.Arrays;

public class ClassBasedRuleBuilder extends RuleBuilder {

    private final Class<?> ruleClass;

    public ClassBasedRuleBuilder(Class<?> ruleClass) {
        super();
        Assert.notNull(ruleClass, "ruleClass cannot be null.");
        this.ruleClass = ruleClass;
        load(RuleDefinition.load(ruleClass));
    }

    private void load(RuleDefinition definition) {
        name(definition.getName());
        description(definition.getDescription());
        target(getObjectFactory().create(definition.getRuleClass()));
        given(ConditionUtils.create(definition.getConditionDefinition(), getTarget()));

        if (definition.getThenActionDefinitions() != null) {
            Arrays.stream(definition.getThenActionDefinitions())
                    .forEach(actionDefinition -> then(ActionUtils.create(actionDefinition, getTarget())));
        }

        otherwise(definition.getElseActionDefinition() != null
                ? ActionUtils.create(definition.getElseActionDefinition(), getTarget())
                : null);
    }

    @Override
    public Class<?> getRuleClass() {
        return ruleClass;
    }
}
