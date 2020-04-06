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
import java.util.ArrayList;
import java.util.List;

/**
 * Tests for Bindings.
 *
 * @author Max Arulananthan
 */
public class BindingsTest {

    public BindingsTest() {
        super();
    }

    @Test
    public void testCreateBindings() {
        Bindings bindings = Bindings.create();
        Assert.assertTrue(bindings instanceof DefaultBindings);
        Bindings scopedBindings = Bindings.createWithScopes();
        Assert.assertTrue(scopedBindings instanceof DefaultScopedBindings);
    }

    @Test
    public void bindTest1() {
        Bindings bindings = Bindings.create();
        Binding binding = BindingBuilder.with("key1").value("Hello World!").build();
        bindings.bind(binding);
        Binding match = bindings.getBinding("key1");
        Assert.assertTrue(match.equals(binding));
    }

    @Test
    public void bindTest2() {
        Bindings bindings = Bindings.create();
        Binding binding = BindingBuilder.with(key1 -> "hello world!").build();
        bindings.bind(binding);
        Binding match = bindings.getBinding("key1");
        Assert.assertTrue(match.equals(binding));
    }

    @Test
    public void bindTest3() {
        Bindings bindings = Bindings.create();
        bindings.bind("key1", 100);
        Binding match = bindings.getBinding("key1");
        Assert.assertTrue(match.getName().equals("key1"));
        Assert.assertTrue(match.getValue().equals(100));
        Assert.assertTrue(match.getType().equals(Integer.class));
    }

    @Test
    public void bindTest4() {
        Bindings bindings = Bindings.create();
        bindings.bind("key1", int.class);
        Binding match = bindings.getBinding("key1");
        Assert.assertTrue(match.getName().equals("key1"));
        Assert.assertTrue(match.getValue().equals(0));
        Assert.assertTrue(match.getType().equals(int.class));
    }

    @Test
    public void bindTest5() {
        Bindings bindings = Bindings.create();
        bindings.bind("x", new TypeReference<List<Integer>>() {});
        Binding match = bindings.getBinding("x");
        Assert.assertTrue(match.getName().equals("x"));
        Assert.assertTrue(match.getType().equals(new TypeReference<List<Integer>>() {}.getType()));
        Assert.assertTrue(match.getValue() == null);
    }

    @Test
    public void bindTest6() {
        Bindings bindings = Bindings.create();
        bindings.bind("x", int.class, 250);
        Binding match = bindings.getBinding("x");
        Assert.assertTrue(match.getName().equals("x"));
        Assert.assertTrue(match.getType().equals(int.class));
        Assert.assertTrue(match.getValue().equals(250));
    }

    @Test
    public void bindTest7() {
        List<Integer> values = new ArrayList<>();
        Bindings bindings = Bindings.create();
        bindings.bind("x", new TypeReference<List<Integer>>() {}, values);
        Binding match = bindings.getBinding("x");
        Assert.assertTrue(match.getName().equals("x"));
        Assert.assertTrue(match.getType().equals(new TypeReference<List<Integer>>() {}.getType()));
        Assert.assertTrue(match.getValue().equals(values));
    }

    @Test
    public void bindTest8() {
        Bindings bindings = Bindings.create();
        Binding binding1 = BindingBuilder.with(key1 -> "hello world!").build();
        Binding binding2 = BindingBuilder.with(key2 -> 25).build();
        Binding binding3 = BindingBuilder.with(key3 -> new BigDecimal("100.00")).build();

    }
}
