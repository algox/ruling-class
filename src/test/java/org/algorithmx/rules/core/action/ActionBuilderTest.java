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
package org.algorithmx.rules.core.action;

import org.algorithmx.rules.annotation.Optional;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.util.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests for ActionBuilder.
 *
 * @author Max Arulananthan
 * @since 1.0
 *
 */
public class ActionBuilderTest {

    public ActionBuilderTest() {
        super();
    }

    @Test
    public void testNoArg() {
        Action action = ActionBuilder
                .with(() -> {
                    return;
                })
                .name("action0")
                .build();

        Assert.assertTrue(action.getMethodDefinition().getName().equals("action0"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 0);
    }

    @Test
    public void test1Arg() {
        Action action = ActionBuilder
                .with((String x) -> {
                    return;
                })
                .description("Action with one arg")
                .build();

        Assert.assertTrue(action.getMethodDefinition().getDescription().equals("Action with one arg"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 1);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[0].getName().equals("x"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[0].getType().equals(String.class));
    }

    @Test
    public void test2Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value) -> {
                    return;
                })
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 2);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[1].getName().equals("value"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[1].getType().equals(BigDecimal.class));
    }

    @Test
    public void test3Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c) -> {
                    return;
                })
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 3);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getName().equals("c"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getType().equals(Integer.class));
    }

    @Test
    public void test4Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d) -> {
                    return;
                })
                .param("d")
                    .optional(true)
                    .build()
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 4);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[3].getName().equals("d"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[3].getType().equals(Float.class));
        Assert.assertTrue(!action.getMethodDefinition().getParameterDefinitions()[3].isRequired());
    }

    @Test
    public void test5Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d, @Optional(defaultValue = "yes") Boolean flag) -> {
                    return;
                })
                .param("d")
                    .optional(true)
                    .build()
                .param("flag")
                    .optional(true)
                    .defaultValueText("yes")
                    .build()
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 5);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[4].getName().equals("flag"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[4].getType().equals(Boolean.class));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[4].getDefaultValueText().equals("yes"));
    }

    @Test
    public void test6Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d,
                       @Optional(defaultValue = "yes") Boolean flag, Binding<String> bindingValue) -> {
                    bindingValue.setValue("Hello world!");
                })
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 6);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[5].getName().equals("bindingValue"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[5].getType().equals(Binding.class));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[5].isBinding());
    }

    @Test
    public void test7Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d,
                       @Optional(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg) -> {
                    bindingValue.setValue("Hello world!");
                })
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 7);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[6].getName().equals("listArg"));
        // Lambda does not store generic info
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[6].getType().equals(List.class));
    }

    @Test
    public void test8Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d,
                       @Optional(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg,
                       Map<String, Object> mapArg) -> {
                    mapArg.put("key", "Hello world!");
                })
                .param(7)
                    .type(new TypeReference<Map<String, Object>>(){}.getType())
                    .build()
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 8);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[7].getName().equals("mapArg"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[7]
                .getType().equals(new TypeReference<Map<String, Object>>(){}.getType()));
    }

    @Test
    public void test9Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d,
                       @Optional(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg,
                       Map<String, Object> mapArg, List<String> someList) -> {
                    mapArg.put("key", "Hello world!");
                })
                .param("someList")
                    .type(new TypeReference<List<String>>(){}.getType())
                    .build()
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 9);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[8].getName().equals("someList"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[8]
                .getType().equals(new TypeReference<List<String>>(){}.getType()));
    }

    @Test
    public void test10Args() {
        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c, @Optional Float d,
                       @Optional(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg,
                       Map<String, Object> mapArg, List<String> someList, String tenthArg) -> {
                    mapArg.put("key", "Hello world!");
                })
                .param("someList")
                    .type(new TypeReference<List<String>>(){}.getType())
                    .build()
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 10);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[9].getName().equals("tenthArg"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[9].getType().equals(String.class));
    }

    @Test(expected = UnrulyException.class)
    public void testInvalidParameterIndex() {
        ActionBuilder
                .with((String x, BigDecimal value, Integer c) -> {
                    return;
                })
                .param(3)
                    .type(new TypeReference<List<String>>(){}.getType())
                    .build()
                .build();

    }

    @Test(expected = UnrulyException.class)
    public void testInvalidParameterNameIndex() {
        ActionBuilder
                .with((String x, BigDecimal value, Integer c) -> {
                    return;
                })
                .param("y")
                    .type(new TypeReference<List<String>>(){}.getType())
                    .build()
                .build();

    }

    @Test
    public void testInnerClass() {
        Action action = ActionBuilder
                .with(new TriAction<String, Integer, Map<String, Integer>>() {
                    @Override
                    public void run(String a, Integer b, Map<String, Integer> map) {

                    }
                })
                .build();
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 3);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getName().equals("map"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2]
                .getType().equals(new TypeReference<Map<String, Integer>>(){}.getType()));
        action.run("aa", 12, new HashMap<>());
    }

    public String c = "c";

    @Test
    public void testOtherArgs() {

        Integer a = 12;
        String b = "b";

        Action action = ActionBuilder
                .with((String x, BigDecimal value, Integer c) -> {
                    a.intValue();
                    b.length();
                    c.intValue();
                    return;
                })
                .build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 3);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getName().equals("c"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getType().equals(Integer.class));
    }

    public static void testActionMethod1(Integer a, List<Integer> values, BigDecimal c) {}

    @Test
    public void testMethodReference1() {
        Action action = ActionBuilder
                .with(ActionBuilderTest::testActionMethod1).build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 3);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[0].getName().equals("a"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[0].getType().equals(Integer.class));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[1].getName().equals("values"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[1].getType().equals(new TypeReference<List<Integer>>(){}.getType()));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getName().equals("c"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[2].getType().equals(BigDecimal.class));
    }

    public void testActionMethod2(List<Integer> values, BigDecimal c) {}

    @Test
    public void testMethodReference2() {
        Action action = ActionBuilder
                .with(this::testActionMethod2).build();

        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions().length == 2);
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[0].getName().equals("values"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[0].getType().equals(new TypeReference<List<Integer>>(){}.getType()));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[1].getName().equals("c"));
        Assert.assertTrue(action.getMethodDefinition().getParameterDefinitions()[1].getType().equals(BigDecimal.class));
    }
}
