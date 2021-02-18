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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class MethodHolder extends RuleHolder<Method> {

    private final List<RuleSet> methodParameterRules = new ArrayList<>();
    private final List<RuleSetBuilder> methodParameterRuleBuilders = new ArrayList<>();
    private final boolean isGetter;

    public MethodHolder(Method method) {
        super(method, method.getName());
        Assert.notNull(method, "method cannot be null.");
        this.isGetter = isGetter(method);

        for (int i = 0; i < method.getParameterCount(); i++) {
            methodParameterRuleBuilders.add(RuleSetBuilder.with(method.getName() + "-" + i + "-Rules"));
        }
    }

    public void addParameterRule(int index, Rule<?> rule) {
        Assert.notNull(rule, "rule cannot be null.");
        Assert.isTrue(index < methodParameterRuleBuilders.size(), "Invalid index [" + index + "]");
        this.methodParameterRuleBuilders.get(index).rule(rule);
    }

    public RuleSet getParameterRules(int index) {
        return methodParameterRules.get(index);
    }

    public int getParameterCount() {
        return methodParameterRules.size();
    }

    public boolean isGetter() {
        return isGetter;
    }

    private boolean isGetter(Method method) {
        return !method.getReturnType().equals(void.class) && method.getParameterCount() == 0 &&
                method.getName().length() > 3 && method.getName().startsWith("get");
    }

    @Override
    public void build() {
        super.build();
        for (RuleSetBuilder builder : methodParameterRuleBuilders) {
            methodParameterRules.add(builder.build());
        }
    }
}
