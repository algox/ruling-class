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

package org.algorithmx.rulii.test.ruleset;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.PreCondition;
import org.algorithmx.rulii.annotation.RuleSet;
import org.algorithmx.rulii.annotation.Rules;
import org.algorithmx.rulii.annotation.StopCondition;
import org.algorithmx.rulii.bind.Binding;
import org.algorithmx.rulii.core.action.ActionBuilder;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.core.ruleset.RuleSetBuilder;

import java.math.BigDecimal;

@RuleSet(name = "TestRuleSet")
@Description("Sample Test RuleSet using a Class")
public class TestRuleSet {

    public TestRuleSet() {
        super();
    }

    @PreCondition
    public boolean preCondition() {
        return true;
    }

    @Rules
    public void load(RuleSetBuilder builder) {
        builder
                .action(ActionBuilder.build(() -> System.err.println("XXX Better Pre Action")))
                .rule(RuleBuilder
                        .given(ConditionBuilder.build((String y) -> y.equals("")))
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(0)))
                        .name("Rule1")
                        .build())
                .rule(RuleBuilder
                        .given(ConditionBuilder.build((String a, BigDecimal x) -> x != null))
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule2")
                        .build())
                .rule(RuleBuilder
                        .given(ConditionBuilder.build((String a, String b, Integer c) -> c == 20 && "hello".equals(b)))
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule3")
                        .build())
                .rule(RuleBuilder
                        .given(ConditionBuilder.TRUE())
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule6")
                        .build())
                .action(ActionBuilder.build(() -> System.err.println("XXX Better Post Action")));
    }

    @StopCondition
    public boolean stopCondition() {
        return false;
    }

}
