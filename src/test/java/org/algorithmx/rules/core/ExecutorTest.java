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

import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.model.ActionDefinition;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.util.LambdaUtils;
import org.junit.Assert;
import org.junit.Test;

import java.lang.invoke.SerializedLambda;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Tests for running the Rules.
 *
 * @author Max Arulananthan
 */
public class ExecutorTest {

    public ExecutorTest() {
        super();
    }

    @Test
    public void test1() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();

        Bindings bindings = Bindings.defaultBindings()
                .bind("id", int.class, 123)
                .bind("closingDate", Date.class, new Date())
                .bind("values", new TypeReference<List<String>>() {}, new ArrayList<>());

        RuleDefinition definition1 = RuleDefinition.load(TestRule5.class);
        TestRule5 rule5 = new TestRule5();
        boolean result = executor.execute(rule5, definition1.getCondition(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy());
        Assert.assertTrue(result);
    }

    @Test
    public void test2() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();

        Bindings bindings = Bindings.defaultBindings()
                .bind("x", int.class, 123)
                .bind("y", String.class, "Hello")
                .bind("z", BigDecimal.class, new BigDecimal("10.00"));

        Condition.Condition3<Integer, String, BigDecimal> rule3 = (Integer x, String y, BigDecimal z) -> x < 10 && y != null && z != null;
        SerializedLambda lambda1 = LambdaUtils.getSerializedLambda(rule3);
        RuleDefinition definition2 = RuleDefinition.load(lambda1, "Rule3", " Test Rule 3");
        boolean result = executor.execute(rule3, definition2.getCondition(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy());
        Assert.assertTrue(!result);
    }

    @Test
    public void test3() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();

        Bindings bindings = Bindings.defaultBindings()
                .bind("x", int.class, 123)
                .bind("y", String.class, "Hello");

        Condition.Condition2<Integer, String> rule2 = (Integer x, String y) -> x > 10 && y != null;
        SerializedLambda lambda = LambdaUtils.getSerializedLambda(rule2);
        RuleDefinition definition = RuleDefinition.load(lambda, "Rule2", " Test Rule 2");
        boolean result = executor.execute(rule2, definition.getCondition(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy());
        Assert.assertTrue(result);
    }

    @Test
    public void test4() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();

        Bindings bindings = Bindings.defaultBindings()
                .bind("id", int.class, 123)
                .bind("closingDate", Date.class, new Date())
                .bind("values", new TypeReference<List<String>>() {}, new ArrayList<>())
                .bind("result", int.class, 0);

        bindings.bind("bindings", TypeReference.with(Bindings.class), bindings, null, false);

        ActionDefinition definition1 = ActionDefinition.load(TestRule5.class);
        TestRule5 rule5 = new TestRule5();

        executor.execute(rule5, definition1.getAction(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy());
        int result = bindings.get("result");
        Assert.assertTrue(result == 2);
    }

    @Test
    public void test5() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();

        Bindings bindings = Bindings.defaultBindings()
                .bind("id", int.class, 123)
                .bind("y", String.class, "Hello")
                .bind("values", new TypeReference<List<String>>() {}, new ArrayList<>())
                .bind("result", int.class, 0);

        bindings.bind("binds", TypeReference.with(Bindings.class), bindings, null, false);

        Then.Then3<Integer, String, Bindings> action = (Integer id, String y, Bindings binds) -> binds.set("result", 10);
        SerializedLambda lambda = LambdaUtils.getSerializedLambda(action);
        ActionDefinition definition = ActionDefinition.load(lambda,"Then!");
        executor.execute(action, definition.getAction(), bindings, BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy());
        int result = bindings.get("result");
        Assert.assertTrue(result == 10);
    }

    @Test
    public void test6() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();
        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        Rule rule = ruleFactory.rule("TestRule", (String x, Integer y) -> y > 10, "test");
        boolean result = rule.isPass(x -> "hello world", y -> 20, z -> 10);
        Assert.assertTrue(result);
    }

    @Test
    public void test7() {
        BindableMethodExecutor executor = BindableMethodExecutor.create();
        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        Rule rule = ruleFactory.rule("TestRule", (String x, Integer y) -> y > 10, "test");
        boolean result = rule.isPass(x -> "hello world", y -> 20, z -> 10);
        Assert.assertTrue(result);
    }
}
