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

import org.algorithmx.rules.annotation.Nullable;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.util.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
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
                .withNoArgs(() -> {
                    return;
                })
                .name("action0")
                .build();

        Assert.assertTrue(action.getActionDefinition().getActionName().equals("action0"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 0);
    }

    @Test
    public void test1Arg() {
        Action action = ActionBuilder
                .with1Arg((String x) -> {
                    return;
                })
                .description("Action with one arg")
                .build();

        Assert.assertTrue(action.getActionDefinition().getDescription().equals("Action with one arg"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 1);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[0].getName().equals("x"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[0].getType().equals(String.class));
    }

    @Test
    public void test2Args() {
        Action action = ActionBuilder
                .with2Args((String x, BigDecimal value) -> {
                    return;
                })
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 2);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[1].getName().equals("value"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[1].getType().equals(BigDecimal.class));
    }

    @Test
    public void test3Args() {
        Action action = ActionBuilder
                .with3Args((String x, BigDecimal value, Integer c) -> {
                    return;
                })
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 3);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[2].getName().equals("c"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[2].getType().equals(Integer.class));
    }

    @Test
    public void test4Args() {
        Action action = ActionBuilder
                .with4Args((String x, BigDecimal value, Integer c, @Nullable Float d) -> {
                    return;
                })
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 4);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[3].getName().equals("d"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[3].getType().equals(Float.class));
        Assert.assertTrue(!action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[3].isRequired());
    }

    @Test
    public void test5Args() {
        Action action = ActionBuilder
                .with5Args((String x, BigDecimal value, Integer c, @Nullable Float d, @Nullable(defaultValue = "yes") Boolean flag) -> {
                    return;
                })
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 5);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[4].getName().equals("flag"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[4].getType().equals(Boolean.class));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[4].getDefaultValue().equals("yes"));
    }

    @Test
    public void test6Args() {
        Action action = ActionBuilder
                .with6Args((String x, BigDecimal value, Integer c, @Nullable Float d,
                            @Nullable(defaultValue = "yes") Boolean flag, Binding<String> bindingValue) -> {
                    bindingValue.setValue("Hello world!");
                })
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 6);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[5].getName().equals("bindingValue"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[5].getType().equals(Binding.class));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[5].isBinding());
    }

    @Test
    public void test7Args() {
        Action action = ActionBuilder
                .with7Args((String x, BigDecimal value, Integer c, @Nullable Float d,
                            @Nullable(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg) -> {
                    bindingValue.setValue("Hello world!");
                })
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 7);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[6].getName().equals("listArg"));
        // Lambda does not store generic info
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[6].getType().equals(List.class));
    }

    @Test
    public void test8Args() {
        Action action = ActionBuilder
                .with8Args((String x, BigDecimal value, Integer c, @Nullable Float d,
                            @Nullable(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg,
                            Map<String, Object> mapArg) -> {
                    mapArg.put("key", "Hello world!");
                })
                .parameterType(7, new TypeReference<Map<String, Object>>(){}.getType())
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 8);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[7].getName().equals("mapArg"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[7]
                .getType().equals(new TypeReference<Map<String, Object>>(){}.getType()));
    }

    @Test
    public void test9Args() {
        Action action = ActionBuilder
                .with9Args((String x, BigDecimal value, Integer c, @Nullable Float d,
                            @Nullable(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg,
                            Map<String, Object> mapArg, List<String> someList) -> {
                    mapArg.put("key", "Hello world!");
                })
                .parameterType("someList", new TypeReference<List<String>>(){}.getType())
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 9);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[8].getName().equals("someList"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[8]
                .getType().equals(new TypeReference<List<String>>(){}.getType()));
    }

    @Test
    public void test10Args() {
        Action action = ActionBuilder
                .with10Args((String x, BigDecimal value, Integer c, @Nullable Float d,
                            @Nullable(defaultValue = "yes") Boolean flag, Binding<String> bindingValue, List<Integer> listArg,
                            Map<String, Object> mapArg, List<String> someList, String tenthArg) -> {
                    mapArg.put("key", "Hello world!");
                })
                .parameterType("someList", new TypeReference<List<String>>(){}.getType())
                .build();

        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 10);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[9].getName().equals("tenthArg"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[9].getType().equals(String.class));
    }

    @Test(expected = UnrulyException.class)
    public void testInvalidParameterIndex() {
        ActionBuilder
                .with3Args((String x, BigDecimal value, Integer c) -> {
                    return;
                })
                .parameterType(3, new TypeReference<List<String>>(){}.getType())
                .build();

    }

    @Test(expected = UnrulyException.class)
    public void testInvalidParameterNameIndex() {
        ActionBuilder
                .with3Args((String x, BigDecimal value, Integer c) -> {
                    return;
                })
                .parameterType("y", new TypeReference<List<String>>(){}.getType())
                .build();

    }

    @Test(expected = UnrulyException.class)
    public void testInnerClass() {
        Action action = ActionBuilder
                .with(new TriAction<String, Integer, Map<String, Integer>>() {
                    @Override
                    public void execute(String a, Integer b, Map<String, Integer> map) {

                    }
                })
                .build();
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions().length == 3);
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[2].getName().equals("map"));
        Assert.assertTrue(action.getActionDefinition().getMethodDefinition().getParameterDefinitions()[2]
                .getType().equals(new TypeReference<Map<String, Integer>>(){}.getType()));
    }
}