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
package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.match.BindingMatchingStrategyType;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.context.RuleContextBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Tests for Condition/Action execution.
 *
 * @author Max Arulananthan
 */
public class ExecutorTest {

    public ExecutorTest() {
        super();
    }

    @Test
    public void test1() {
        List<Integer> values = new ArrayList<>();

        Rule rule = RuleBuilder.with(
                ConditionBuilder.build((String x, Integer y, List<String> a) -> y > 10))
                .name("rule1")
                .build();

        boolean result = rule.getCondition().isTrue("hello world", 20, values);
        Assert.assertTrue(result);
    }

    @Test
    public void test2() {
        Rule rule = RuleBuilder.with(
                ConditionBuilder.build((String x, Integer y) -> y > 10))
                .name("rule1")
                .build();
        boolean result = rule.getCondition().isTrue("hello world", 20);
        Assert.assertTrue(result);
    }

    @Test
    public void test3() {
        Bindings bindings = Bindings.create()
                .bind("x", String.class)
                .bind("y", int.class, 123)
                .bind("z", String.class, "Hello");

        Rule rule = RuleBuilder.with(
                ConditionBuilder.build((String x, Integer y) -> y > 10))
                .name("rule1")
                .build();

        RuleContext ctx = RuleContextBuilder
                .with(bindings)
                .matchUsing(BindingMatchingStrategyType.MATCH_BY_NAME)
                .build();

        rule.run(ctx);
    }

    @Test
    public void test4() {

        Rule<TestRule1> rule = RuleBuilder.with(TestRule1.class)
                .name("rule1")
                .build();

        TestRule1 x = rule.getTarget();
        Assert.assertNotNull(x);
    }

    @Test
    public void test5() {
        Bindings bindings = Bindings.create()
                .bind("x", Integer.class, 101);

        Rule<TestRule5> rule = RuleBuilder.with(TestRule5.class)
                .name("rule1")
                .build();

        rule.run(RuleContextBuilder.build(bindings));
        Assert.assertTrue(bindings.getValue("x", Integer.class) == 0);
    }
}
