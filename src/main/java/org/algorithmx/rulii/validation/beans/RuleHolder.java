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

package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.ruleset.RuleSet;
import org.algorithmx.rulii.core.ruleset.RuleSetBuilder;
import org.algorithmx.rulii.lib.spring.util.Assert;

class RuleHolder<T> {
    private final T type;
    private final String name;
    private final RuleSetBuilder builder;
    private RuleSet ruleSet;

    public RuleHolder(T type, String name) {
        super();
        Assert.notNull(type, "type cannot be null.");
        Assert.notNull(name, "name cannot be null.");
        this.type = type;
        this.name = name;
        this.builder = RuleSetBuilder.with(name);
    }

    public void addRule(Rule<?> rule) {
        Assert.notNull(rule, "rule cannot be null.");
        builder.rule(rule);
    }

    public T getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public RuleSetBuilder getBuilder() {
        return builder;
    }

    public RuleSet getRuleSet() {
        if (this.ruleSet == null) build();
        return ruleSet;
    }

    public void build() {
        this.ruleSet = builder.build();
    }

}
