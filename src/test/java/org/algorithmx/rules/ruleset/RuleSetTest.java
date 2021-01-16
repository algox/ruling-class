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
package org.algorithmx.rules.ruleset;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSet;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * Tests for RuleSets.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class RuleSetTest {

    public RuleSetTest() {
        super();
    }

    @Test
    public void test1() {
        Bindings bindings = Bindings.create()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));

        Rule rule6 = RuleBuilder
                .given(ConditionBuilder.TRUE())
                .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                .name("Rule6")
                .build();

        RuleSet rules = RuleSetBuilder
                .with("RuleSet1", "Test Rule Set")
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
                .rule(rule6)
                .build();

        Rule rule2 = rules.get("Rule2", Rule.class);
        Rule rule3 = rules.get("Rule3", Rule.class);
        rules.run(bindings);

        Assert.assertNotNull(rule2);
        Assert.assertNotNull(rule3);
        Assert.assertTrue(bindings.getValue("c", Integer.class) == 2);
        Assert.assertTrue(rule3.getCondition().isTrue("", "hello", 20));
    }

    @Test
    public void test2() {
        Bindings bindings = Bindings.create()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));

        RuleSet rules = RuleSetBuilder.build(TestRuleSet.class);

        Rule rule2 = rules.get("Rule2", Rule.class);
        Rule rule3 = rules.get("Rule3", Rule.class);
        rules.run(bindings);

        Assert.assertNotNull(rule2);
        Assert.assertNotNull(rule3);
        Assert.assertTrue(bindings.getValue("c", Integer.class) == 2);
        Assert.assertTrue(rule3.getCondition().isTrue("", "hello", 20));
    }
}
