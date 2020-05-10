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
package org.algorithmx.rules.core.rule;

import org.algorithmx.rules.lib.spring.util.Assert;

public class ObjectBasedRuleBuilder extends RuleBuilder {

    private final Object target;

    public ObjectBasedRuleBuilder(Object target) {
        super();
        Assert.notNull(target, "target cannot be null.");
        this.target = target;
        load(target);
    }

    private void load(Object target) {
        target(target);

        RuleDefinition definition = RuleDefinition.load(target.getClass());
        //given(ConditionUtils.create(definition.getConditionDefinition(), target));
        name(definition.getName());
        description(definition.getDescription());

        /*if (definition.getThenActionDefinitions() != null) {
            Arrays.stream(definition.getThenActionDefinitions())
                    .forEach(actionDefinition -> then(ActionUtils.create(actionDefinition, target)));
        }

        otherwise(definition.getElseActionDefinition() != null
                ? ActionUtils.create(definition.getElseActionDefinition(), target)
                : null);*/
    }

    @Override
    public Class<?> getRuleClass() {
        return target.getClass();
    }
}
