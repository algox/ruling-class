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

package org.algorithmx.rulii.test.core;

import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.match.BindingMatchingStrategyType;
import org.algorithmx.rulii.bind.match.ParameterResolver;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
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

        Rule rule = RuleBuilder
                .name("rule1")
                .given(ConditionBuilder.build((String x, Integer y, List<String> a) -> y > 10))
                .build();

        boolean result = rule.getCondition().isTrue("hello world", 20, values);
        Assert.assertTrue(result);
    }

    @Test
    public void test2() {
        Rule rule = RuleBuilder
                .name("rule1")
                .given(ConditionBuilder.build((String x, Integer y) -> y > 10))
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

        Rule rule = RuleBuilder
                .name("rule1")
                .given(ConditionBuilder.build((String x, Integer y) -> y > 10))
                .build();

        RuleContext context = RuleContextBuilder
                .with(bindings)
                .matchUsing(BindingMatchingStrategyType.MATCH_BY_NAME)
                .build();

        rule.run(context);
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
                .bind("x", Integer.class, 125);

        Rule<TestRule5> rule = RuleBuilder.with(TestRule5.class)
                .name("rule1")
                .build();

        rule.run(RuleContextBuilder
                .with(bindings)
                .paramResolver(ParameterResolver.create())
                .build());
        Assert.assertTrue(bindings.getValue("x", Integer.class) == 0);
    }
}
