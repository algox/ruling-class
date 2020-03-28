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
import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.build.RuleBuilder;
import org.algorithmx.rules.core.condition.ConditionBuilder;
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
        BindableMethodExecutor executor = BindableMethodExecutor.defaultBindableMethodExecutor();
        ParameterResolver resolver = ParameterResolver.defaultParameterResolver();

        Bindings bindings = Bindings.defaultBindings()
                .bind("id", int.class, 123)
                .bind("birthDate", Date.class, new Date())
                .bind("values", new TypeReference<List<String>>() {}, new ArrayList<>());

        RuleDefinition definition1 = RuleDefinition.load(TestRule5.class);
        TestRule5 rule5 = new TestRule5();
        boolean result = executor.execute(rule5, definition1.getConditionDefinition().getMethodDefinition(),
                resolver.resolveAsBindingValues(definition1.getConditionDefinition().getMethodDefinition(), bindings,
                        BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy()));
        Assert.assertTrue(result);
    }

    @Test
    public void test2() {
        BindableMethodExecutor executor = BindableMethodExecutor.defaultBindableMethodExecutor();
        ParameterResolver resolver = ParameterResolver.defaultParameterResolver();

        Bindings bindings = Bindings.defaultBindings()
                .bind("x", int.class, 123)
                .bind("y", String.class, "Hello")
                .bind("z", BigDecimal.class, new BigDecimal("10.00"));

        ConditionBuilder builder = ConditionBuilder.with3Args((Integer x, String y, BigDecimal z) -> x < 10 && y != null && z != null);

        Rule rule = RuleBuilder.with(
                builder.build())
                .name("Rule3")
                .description("Test Rule 3")
                .build();

        RuleDefinition definition2 = rule.getRuleDefinition();
        boolean result = executor.execute(builder.build(), definition2.getConditionDefinition().getMethodDefinition(),
                resolver.resolveAsBindingValues(definition2.getConditionDefinition().getMethodDefinition(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy()));
        Assert.assertTrue(!result);
    }

    @Test
    public void test3() {
        BindableMethodExecutor executor = BindableMethodExecutor.defaultBindableMethodExecutor();
        ParameterResolver resolver = ParameterResolver.defaultParameterResolver();

        Bindings bindings = Bindings.defaultBindings()
                .bind("x", int.class, 123)
                .bind("y", String.class, "Hello");

        Rule rule = RuleBuilder.with(
                ConditionBuilder.with2Args((Integer x, String y) -> x > 10 && y != null).build())
                .name("Rule2")
                .description("Test Rule 2")
                .build();


        RuleDefinition definition = rule.getRuleDefinition();

        boolean result = executor.execute(rule, definition.getConditionDefinition().getMethodDefinition(),
                resolver.resolveAsBindingValues(definition.getConditionDefinition().getMethodDefinition(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy()));
        Assert.assertTrue(result);
    }

    @Test
    public void test4() {
        BindableMethodExecutor executor = BindableMethodExecutor.defaultBindableMethodExecutor();
        ParameterResolver resolver = ParameterResolver.defaultParameterResolver();

        Bindings bindings = Bindings.defaultBindings()
                .bind("id", int.class, 123)
                .bind("birthDate", Date.class, new Date())
                .bind("values", new TypeReference<List<String>>() {}, new ArrayList<>())
                .bind("result", int.class, 0);

        bindings.bind("bindings", TypeReference.with(Bindings.class), bindings, false);

        ActionDefinition[] definition1 = ActionDefinition.loadThenActions(TestRule5.class);
        TestRule5 rule5 = new TestRule5();

        Assert.assertTrue(definition1.length == 1);
        executor.execute(rule5, definition1[0].getMethodDefinition(),
                resolver.resolveAsBindingValues(definition1[0].getMethodDefinition(), bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy()));
        int result = bindings.get("result");
        Assert.assertTrue(result == 2);
    }

    @Test
    public void test5() {
        BindableMethodExecutor executor = BindableMethodExecutor.defaultBindableMethodExecutor();
        ParameterResolver resolver = ParameterResolver.defaultParameterResolver();

        Bindings bindings = Bindings.defaultBindings()
                .bind("id", int.class, 123)
                .bind("y", String.class, "Hello")
                .bind("values", new TypeReference<List<String>>() {}, new ArrayList<>())
                .bind("result", int.class, 0);

        bindings.bind("binds", TypeReference.with(Bindings.class), bindings, false);

        ActionConsumer.ActionConsumer3<?, ?, ?> action = (Integer id, List<String> values, Bindings binds) -> binds.set("result", 10);
        SerializedLambda lambda = LambdaUtils.getSerializedLambda(action);
        ActionDefinition definition = ActionDefinition.load(lambda,"ActionConsumer!");
        executor.execute(action, definition.getMethodDefinition(),
                resolver.resolveAsBindingValues(definition.getMethodDefinition(), bindings,
                        BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy()));
        int result = bindings.get("result");
        Assert.assertTrue(result == 10);
    }

    @Test
    public void test6() {
        // TODO : Fix generic mapping with lambdas
        List<Integer> values = new ArrayList<>();

        Rule rule = RuleBuilder.with(
                ConditionBuilder.with3Args((String x, Integer y, List<String> a) -> y > 10).build())
                .name("rule1")
                .build();

        boolean result = rule.isPass("hello world", 20, values);
        Assert.assertTrue(result);
    }

    @Test
    public void test7() {
        Rule rule = RuleBuilder.with(
                ConditionBuilder.with2Args((String x, Integer y) -> y > 10).build())
                .name("rule1")
                .build();
        boolean result = rule.isPass("hello world", 20);
        Assert.assertTrue(result);
    }
}
