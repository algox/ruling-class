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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

class CtorHolder extends RuleHolder<Constructor> {

    private final List<RuleSetBuilder> ctorParameterRuleBuilders = new ArrayList<>();
    private final List<RuleSet> ctorParameterRules = new ArrayList<>();

    public CtorHolder(Constructor ctor) {
        super(ctor, "ctorRules");
        for (int i = 0; i < ctor.getParameterCount(); i++) {
            ctorParameterRuleBuilders.add(RuleSetBuilder.with("ctor-" + i + "-Rules"));
        }
    }

    public void addParameterRule(int index, Rule<?> rule) {
        Assert.notNull(rule, "rule cannot be null.");
        Assert.isTrue(index < ctorParameterRuleBuilders.size() - 1, "Invalid index [" + index + "]");
        this.ctorParameterRuleBuilders.get(index).rule(rule);
    }

    public RuleSet getParameterRules(int index) {
        return ctorParameterRules.get(index);
    }

    @Override
    public void build() {
        super.build();

        for (RuleSetBuilder builder : ctorParameterRuleBuilders) {
            this.ctorParameterRules.add(builder.build());
        }
    }
}
