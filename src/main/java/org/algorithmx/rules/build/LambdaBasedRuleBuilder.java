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

import org.algorithmx.rules.core.Action;
import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.spring.util.Assert;

import java.util.ArrayList;
import java.util.List;

public class LambdaBasedRuleBuilder {

    private String name;
    private String description;
    private Condition condition;
    private List<Action> thenActions = new ArrayList<>();
    private Action otherwiseAction;

    private LambdaBasedRuleBuilder(Condition condition) {
        super();
        Assert.notNull(condition, "condition cannot be null.");
        this.condition = condition;
    }

    public static LambdaBasedRuleBuilder withCondition(Condition condition) {
        return new LambdaBasedRuleBuilder(condition);
    }

    /**
     * Name of the Rule.
     *
     * @param name name of the Rule.
     * @return LambdaBasedRuleBuilder for fluency.
     */
    public LambdaBasedRuleBuilder name(String name) {
        this.name = name;
        return this;
    }

    /**
     * Rule description.
     *
     * @param description Rule description.
     * @return LambdaBasedRuleBuilder for fluency.
     */
    public LambdaBasedRuleBuilder description(String description) {
        this.description = description;
        return this;
    }

    public LambdaBasedRuleBuilder then(Action action) {
        Assert.notNull(action, "action cannot be null.");
        this.thenActions.add(action);
        return this;
    }

    public LambdaBasedRuleBuilder otherwise(Action action) {
        Assert.notNull(action, "action cannot be null.");
        this.otherwiseAction = action;
        return this;
    }

    public Rule build() {
        return null;
    }
}
