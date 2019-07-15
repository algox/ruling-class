/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Basic Binding tests.
 *
 * @author Max Arulananthan
 */
public class BindTest {

    public BindTest() {
        super();
    }

    @Test
    public void testBind1() {
        Bindings bindings = Bindings.create();
        Binding var = bindings.bind("key", String.class, "value");
        Assert.assertEquals("key", var.getName());
        Assert.assertEquals(String.class, var.getType());
        Assert.assertEquals("value", var.getValue());
    }

    @Test
    public void testBind2() {
        Bindings bindings = Bindings.create();
        Binding<Double> var = bindings.bind("key", Double.class);
        var.setValue(33.33);
        double result = var.getValue();
        Assert.assertEquals(33.33, result, 0.00);
    }

    @Test
    public void testBind3() {
        Bindings ctx = Bindings.create();
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);
        Binding var = ctx.bind("key", new TypeReference<List<Integer>>(){});
        var.setValue(values);
        Assert.assertEquals(values, var.getValue());
    }

    @Test(expected = BindingAlreadyExistsException.class)
    public void testBind4() {
        Bindings ctx = Bindings.create();
        ctx.bind("key", String.class, "value");
        ctx.bind("key", new TypeReference<List<Integer>>(){});
    }

    @Test
    public void testBind5() {
        Bindings ctx = Bindings.create();
        ctx.bind("key1", String.class, "value");
        ctx.bind("key2", new TypeReference<List<Integer>>(){});
        ctx.bind("key3", BigDecimal.class);
        Assert.assertEquals(3, ctx.size());
        ctx.clear();
        Assert.assertEquals(0, ctx.size());
    }

    @Test
    public void testBind6() {
        Bindings ctx = Bindings.create();
        ctx.bind("key1", String.class, "value");
        ctx.bind("key2", new TypeReference<List<Integer>>(){});
        Assert.assertTrue(ctx.contains("key1"));
        Assert.assertTrue(ctx.contains("key2"));
    }

    @Test
    public void testBind7() {
        Bindings ctx = Bindings.create();
        ctx.bind("key1", String.class, "value");
        ctx.bind("key2", new TypeReference<List<?>>(){});
        ctx.bind("key3", BigDecimal.class);
        ctx.bind("key4", new TypeReference<Map<? extends List, List<Integer>>>(){});

        Assert.assertTrue(ctx.contains("key1", String.class));
        Assert.assertTrue(ctx.contains("key2", new TypeReference<List<Integer>>(){}.getType()));
        Assert.assertTrue(ctx.contains("key3", BigDecimal.class));
        Assert.assertTrue(ctx.contains("key4", new TypeReference<Map<ArrayList, List<Integer>>>(){}.getType()));
    }

    @Test
    public void testBind8() {
        Bindings ctx = Bindings.create();
        ctx.bind("key1", String.class, "value");
        ctx.bind("key2", new TypeReference<List<?>>(){});
        ctx.bind("key3", BigDecimal.class, new BigDecimal("10.00"));
        ctx.bind("key4", new TypeReference<Map<? extends List, List<Integer>>>(){});

        Binding binding = ctx.getBinding("key3");

        Assert.assertNotNull(binding);
        Assert.assertEquals(binding.getValue(), new BigDecimal("10.00"));
    }

    @Test
    public void testBind9() {
        Bindings ctx = Bindings.create();
        ctx.bind("key1", String.class, "value");
        ctx.bind("key2", new TypeReference<List<?>>() {});
        ctx.bind("key3", BigDecimal.class, new BigDecimal("10.00"));
        ctx.bind("key4", new TypeReference<Map<? extends List, List<Integer>>>() {});
        ctx.bind("key5", BigDecimal.class, new BigDecimal("20.00"));

        Set<Binding> bindings1 = ctx.getBindings(BigDecimal.class);
        Assert.assertTrue(bindings1.size() == 2);
        Set<Binding> bindings2 = ctx.getBindings(new TypeReference<Map<? extends List, List<Integer>>>() {}.getType());
        Assert.assertTrue(bindings2.size() == 1);
    }

    @Test(expected = InvalidBindingException.class)
    public void testValidation() {
        Bindings ctx = Bindings.create();
        ctx.bind("key", String.class, "hello world!", (Object s) -> !((String) s).contains("hello"));
    }

}

