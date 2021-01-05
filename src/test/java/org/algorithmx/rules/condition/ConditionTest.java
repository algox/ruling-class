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
package org.algorithmx.rules.condition;

import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.util.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests related to the ConditionBuilder.
 *
 * @author Max Arulananthan.
 */
public class ConditionTest {

    public ConditionTest() {
        super();
    }

    @Test
    public void testCondition0() {
        Condition condition = ConditionBuilder
                .with(() -> true)
                .build();
        Assert.assertTrue(condition.isTrue());
    }

    @Test
    public void testCondition2() {
        Condition condition = ConditionBuilder
                .with((Integer a, String x) -> a > 55)
                .build();
        Assert.assertTrue(condition.isTrue(56, "abc"));
    }

    @Test
    public void testCondition3() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x) -> a != null)
                .build();
        Assert.assertTrue(condition.isTrue(10, new Date(), "abc"));
    }

    @Test
    public void testCondition4() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values) -> a > 35 && x.equals("x")
                        && values.get(0).equals("one"))
                .param(3)
                    .type(new TypeReference<List<String>>(){}.getType())
                    .build()
                .build();
        List<String> values = new ArrayList<>();
        values.add("one");
        Assert.assertTrue(condition.isTrue(36, new Date(), "x", values));
    }

    @Test
    public void testCondition5() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values, Long longValue) -> a > 35 && x.equals("x")
                        && values.get(0).equals("one"))
                .param("values")
                    .type(new TypeReference<List<String>>(){}.getType())
                    .build()
                .build();
        List<String> values = new ArrayList<>();
        values.add("one");
        Assert.assertTrue(condition.isTrue(36, new Date(), "x", values, 0l));
    }

    @Test
    public void testCondition6() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values, Long longValue, String b) -> a > 35 && x.equals("x"))
                .build();
        Assert.assertTrue(condition.isTrue(36, new Date(), "x", new ArrayList<>(), 0l, "b"));
    }

    @Test
    public void testCondition7() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values, Long longValue, String b, BigDecimal big)
                        -> big.compareTo(BigDecimal.ZERO) > 0)
                .build();
        Assert.assertTrue(condition.isTrue(36, new Date(), "x", new ArrayList<>(), 0l, "b", new BigDecimal("100")));
    }

    @Test
    public void testCondition8() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values, Long longValue, String b,
                            BigDecimal big, List<Integer> numbers) ->
                {
                    return numbers != null && numbers.stream().count() > 5;
                })
                .build();
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        Assert.assertTrue(condition.isTrue(36, new Date(), "x", new ArrayList<>(), 0l, "b",
                new BigDecimal("100"), numbers));
    }

    @Test
    public void testCondition9() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values, Long longValue, String b,
                            BigDecimal big, List<Integer> numbers, Map<String, String> map)
                        -> map.containsKey("abcde")
                )
                .description("some description")
                .build();
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        Map<String, String> map = new HashMap<>();
        map.put("abcde", "xxx");

        Assert.assertTrue(condition.getMethodDefinition().getDescription().equals("some description"));
    }

    @Test
    public void testCondition10() {
        Condition condition = ConditionBuilder
                .with((Integer a, Date date, String x, List<String> values, Long longValue, String b,
                            BigDecimal big, List<Integer> numbers, Map<String, String> map, Integer z)
                        -> map.containsKey("abcde")
                )
                .description("some description")
                .build();
        List<Integer> numbers = new ArrayList<>();
        numbers.add(1);
        numbers.add(2);
        numbers.add(3);
        numbers.add(4);
        numbers.add(5);
        numbers.add(6);
        Map<String, String> map = new HashMap<>();
        map.put("abcde", "xxx");

        Assert.assertTrue(condition.isTrue(36, new Date(), "x", new ArrayList<>(), 0l, "b",
                new BigDecimal("100"), numbers, map, 10));
    }

    @Test
    public void testConditionConsumer() {
        Condition condition = ConditionBuilder.build((Integer a) -> a > 10);
        Assert.assertTrue(condition.isTrue(13));
    }
}
