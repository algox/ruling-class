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
package org.algorithmx.rules.bind.match;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ScopedBindings;
import org.algorithmx.rules.util.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Tests for BindingMatchingStrategy.
 *
 * @author Max Arulananthan
 */
public class BindingMatchingStrategyTest {

    public BindingMatchingStrategyTest() {
        super();
    }

    @Test
    public void testMatch1() {
        Bindings bindings = Bindings.create()
                .bind("key1", String.class)
                .bind("key2", Integer.class)
                .bind("key3", BigDecimal.class)
                .bind("key4", new TypeReference<List<Long>>() {})
                .bind("key5", new TypeReference<Map<?, ?>>() {});

        Binding<Integer> var2 = bindings.getBinding("key2");
        Binding<Map<?, ?>> var5 = bindings.getBinding("key5");

        Map<String, Binding<Object>> matches = BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy().match(bindings,
                "key2", Object.class);
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.containsValue(var2));

        matches = BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy().match(bindings, "key5", Object.class);
        Assert.assertTrue(matches.size() == 1);
        Assert.assertTrue(matches.containsValue(var5));

        matches = BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy().match(bindings, "key6", Object.class);
        Assert.assertTrue(matches.size() == 0);
    }

    @Test
    public void testMatch2() {
        Bindings bindings = Bindings.create()
                .bind("key1", String.class)
                .bind("key2", Integer.class)
                .bind("key3", BigDecimal.class)
                .bind("key4", new TypeReference<List<Long>>() {})
                .bind("key5", new TypeReference<Map<?, ?>>() {})
                .bind("key6", new TypeReference<Map<String, String>>() {});

        Binding<String> var1 = bindings.getBinding("key1");
        Binding<Integer> var2 = bindings.getBinding("key2");
        Binding<BigDecimal> var3 = bindings.getBinding("key3");
        Binding<List<Long>> var4 = bindings.getBinding("key4");
        Binding<Map<?, ?>> var5 = bindings.getBinding("key5");
        Binding<Map<String, String>> var6 = bindings.getBinding("key6");

        Map<String, Binding<Integer>> matches1 = BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy().match(bindings,
                null, Integer.class);
        Assert.assertTrue(matches1.size() == 1);
        Assert.assertTrue(matches1.containsValue(var2));

        Map<String, Binding<Number>> matches2 = BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy().match(bindings,
                null, Number.class);
        Assert.assertTrue(matches2.size() == 2);
        Assert.assertTrue(matches2.containsValue(var3) && matches2.containsValue(var2));

        Map<String, Binding<Map<?, ?>>> matches3 = BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy().match(bindings,
                null, new TypeReference<Map<?, ?>>() {});
        Assert.assertTrue(matches3.size() == 2);
        Assert.assertTrue(matches3.containsValue(var5) && matches3.containsValue(var6));

        Map<String, Binding<List<Integer>>> matches4 = BindingMatchingStrategyType.MATCH_BY_TYPE
                .getStrategy().match(bindings, null, new TypeReference<List<Integer>>() {});
        Assert.assertTrue(matches4.size() == 0);
    }

    @Test
    public void testMatch3() {
        Bindings bindings = Bindings.create()
                .bind("key1", String.class)
                .bind("key2", Integer.class)
                .bind("key3", BigDecimal.class)
                .bind("key4", new TypeReference<List<Long>>() {})
                .bind("key5", new TypeReference<Map<?, ?>>() {})
                .bind("key6", new TypeReference<Map<String, String>>() {});

        Binding<String> var1 = bindings.getBinding("key1");
        Binding<Integer> var2 = bindings.getBinding("key2");
        Binding<BigDecimal> var3 = bindings.getBinding("key3");
        Binding<List<Long>> var4 = bindings.getBinding("key4");
        Binding<Map<?, ?>> var5 = bindings.getBinding("key5");
        Binding<Map<String, String>> var6 = bindings.getBinding("key6");

        Map<String, Binding<Integer>> matches1 = BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "key1", Integer.class);
        Assert.assertTrue(matches1.size() == 1);
        Assert.assertTrue(matches1.containsValue(var1));

        Map<String, Binding<BigDecimal>> matches2 = BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "x", BigDecimal.class);
        Assert.assertTrue(matches2.size() == 1);
        Assert.assertTrue(matches2.containsValue(var3));

        Map<String, Binding<Map<?, ?>>> matches3 = BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "x", new TypeReference<Map<?, ?>>() {});
        Assert.assertTrue(matches3.size() == 2);
        Assert.assertTrue(matches3.containsValue(var5) && matches3.containsValue(var6));

        Map<String, Binding<List<Integer>>> matches4 = BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE
                .getStrategy().match(bindings, "x", new TypeReference<List<Integer>>() {});
        Assert.assertTrue(matches4.size() == 0);
    }

    @Test
    public void testMatch5() {
        Bindings bindings = Bindings.create()
                .bind("key1", String.class)
                .bind("key2", Integer.class)
                .bind("key3", BigDecimal.class)
                .bind("key4", new TypeReference<List<Long>>() {})
                .bind("key5", new TypeReference<Map<?, ?>>() {})
                .bind("key6", new TypeReference<Map<String, String>>() {});

        Binding<String> var1 = bindings.getBinding("key1");
        Binding<Integer> var2 = bindings.getBinding("key2");
        Binding<BigDecimal> var3 = bindings.getBinding("key3");
        Binding<List<Long>> var4 = bindings.getBinding("key4");
        Binding<Map<?, ?>> var5 = bindings.getBinding("key5");
        Binding<Map<String, String>> var6 = bindings.getBinding("key6");

        Map<String, Binding<Number>> matches1 = BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy()
                .match(bindings, "key3", Number.class);
        Assert.assertTrue(matches1.size() == 1);
        Assert.assertTrue(matches1.containsValue(var3));

        Map<String, Binding<Number>> matches2 = BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE_THEN_BY_JUST_BY_TYPE
                .getStrategy().match(bindings, "key7", Number.class);
        Assert.assertTrue(matches2.size() == 2);
        Assert.assertTrue(matches2.containsValue(var3) && matches2.containsValue(var2));
    }

}
