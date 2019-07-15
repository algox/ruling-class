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
package org.algorithmx.rules.bind;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.*;

/**
 * Binding matching test cases.
 *
 * @author Max Arulananthan
 */
public class BindingMatchingStrategyTest {

    public BindingMatchingStrategyTest() {
        super();
    }

    @Test
    public void testMatch1() {
        Bindings bindings = Bindings.create();

        Binding<String> var1 = bindings.bind("key1", String.class);
        Binding<Integer> var2 = bindings.bind("key2", Integer.class);
        Binding<BigDecimal> var3 = bindings.bind("key3", BigDecimal.class);
        Binding<List<Long>> var4 = bindings.bind("key4", new TypeReference<List<Long>>() {});
        Binding<Map<?, ?>> var5 = bindings.bind("key5", new TypeReference<Map<?, ?>>() {});

        Set<Binding> matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME
                .getStrategy().match(bindings, "key2", null)));
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.contains(var2));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME
                .getStrategy().match(bindings, "key5", null)));
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.contains(var5));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME
                .getStrategy().match(bindings, "key6", null)));
        Assert.assertTrue(matches.size() == 0);
    }

    @Test
    public void testMatch2() {
        Bindings bindings = Bindings.create();

        Binding<String> var1 = bindings.bind("key1", String.class);
        Binding<Integer> var2 = bindings.bind("key2", Integer.class);
        Binding<BigDecimal> var3 = bindings.bind("key3", BigDecimal.class);
        Binding<List<Long>> var4 = bindings.bind("key4", new TypeReference<List<Long>>() {});
        Binding<Map<?, ?>> var5 = bindings.bind("key5", new TypeReference<Map<?, ?>>() {});
        Binding<Map<String, String>> var6 = bindings.bind("key6", new TypeReference<Map<String, String>>() {});

        Set<Binding> matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy()
                .match(bindings, null, Integer.class)));
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.contains(var2));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_TYPE
                .getStrategy().match(bindings, null, Number.class)));
        Assert.assertTrue(matches.size() == 2);
        Assert.assertTrue(matches.contains(var3) && matches.contains(var2));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_TYPE
                .getStrategy().match(bindings, null, new TypeReference<Map<?, ?>>() {}.getType())));
        Assert.assertTrue(matches.size() == 2);
        Assert.assertTrue(matches.contains(var5) && matches.contains(var6));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_TYPE
                .getStrategy().match(bindings, null,
                new TypeReference<List<Integer>>() {}.getType())));
        Assert.assertTrue(matches.size() == 0);
    }

    @Test
    public void testMatch3() {
        Bindings bindings = Bindings.create();

        Binding<String> var1 = bindings.bind("key1", String.class);
        Binding<Integer> var2 = bindings.bind("key2", Integer.class);
        Binding<BigDecimal> var3 = bindings.bind("key3", BigDecimal.class);
        Binding<List<Long>> var4 = bindings.bind("key4", new TypeReference<List<Long>>() {});
        Binding<Map<?, ?>> var5 = bindings.bind("key5", new TypeReference<Map<?, ?>>() {});
        Binding<Map<String, String>> var6 = bindings.bind("key6", new TypeReference<Map<String, String>>() {});

        Set<Binding> matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "key1", Integer.class)));
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.contains(var1));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "x", BigDecimal.class)));
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.contains(var3));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "x",
                new TypeReference<Map<?, ?>>() {}.getType())));
        Assert.assertTrue(matches.size() == 2);
        Assert.assertTrue(matches.contains(var5) && matches.contains(var6));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "x",
                new TypeReference<List<Integer>>() {}.getType())));
        Assert.assertTrue(matches.size() == 0);
    }

    @Test
    public void testMatch5() {
        Bindings bindings = Bindings.create();

        Binding<String> var1 = bindings.bind("key1", String.class);
        Binding<Integer> var2 = bindings.bind("key2", Integer.class);
        Binding<BigDecimal> var3 = bindings.bind("key3", BigDecimal.class);
        Binding<List<Long>> var4 = bindings.bind("key4", new TypeReference<List<Long>>() {});
        Binding<Map<?, ?>> var5 = bindings.bind("key5", new TypeReference<Map<?, ?>>() {});
        Binding<Map<String, String>> var6 = bindings.bind("key6", new TypeReference<Map<String, String>>() {});

        Set<Binding> matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE
                .getStrategy().match(bindings, "key3", Number.class)));
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.contains(var3));

        matches = new HashSet<>(Arrays.asList(BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE_THEN_BY_JUST_BY_TYPE
                .getStrategy().match(bindings, "key7", Number.class)));
        Assert.assertTrue(matches.size() == 2);
        Assert.assertTrue(matches.contains(var3) && matches.contains(var2));

    }
}