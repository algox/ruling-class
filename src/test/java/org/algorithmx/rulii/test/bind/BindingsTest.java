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

package org.algorithmx.rulii.test.bind;

import org.algorithmx.rulii.bind.Binding;
import org.algorithmx.rulii.bind.BindingAlreadyExistsException;
import org.algorithmx.rulii.bind.BindingBuilder;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.bind.DefaultBindings;
import org.algorithmx.rulii.bind.DefaultScopedBindings;
import org.algorithmx.rulii.bind.InvalidBindingException;
import org.algorithmx.rulii.bind.NoSuchBindingException;
import org.algorithmx.rulii.bind.ScopedBindings;
import org.algorithmx.rulii.util.TypeReference;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        Bindings scopedBindings = ScopedBindings.create();
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
        bindings.bind("x", new TypeReference<List<Integer>>() {
        });
        Binding match = bindings.getBinding("x");
        Assert.assertTrue(match.getName().equals("x"));
        Assert.assertTrue(match.getType().equals(new TypeReference<List<Integer>>() {
        }.getType()));
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
        bindings.bind("x", new TypeReference<List<Integer>>() {
        }, values);
        Binding match = bindings.getBinding("x");
        Assert.assertTrue(match.getName().equals("x"));
        Assert.assertTrue(match.getType().equals(new TypeReference<List<Integer>>() {
        }.getType()));
        Assert.assertTrue(match.getValue().equals(values));
    }

    @Test
    public void bindTest8() {
        Bindings bindings = Bindings.create();
        Assert.assertTrue(bindings.size() == 0);
        bindings.bind(BindingBuilder.with(key1 -> "hello world!").build());
        Assert.assertTrue(bindings.size() == 1);
        bindings.bind(BindingBuilder.with(key2 -> 25).build());
        Assert.assertTrue(bindings.size() == 2);
        bindings.bind(BindingBuilder.with(key3 -> new BigDecimal("100.00")).build());
        Assert.assertTrue(bindings.size() == 3);
    }

    @Test
    public void bindTest9() {
        Bindings bindings = Bindings.create();
        bindings.bind(BindingBuilder.with(key1 -> "hello world!").build());
        bindings.bind(BindingBuilder.with(key2 -> 25).build());
        bindings.bind(BindingBuilder.with(key3 -> new BigDecimal("100.00")).build());
        Assert.assertTrue(bindings.size() == 3);
    }

    @Test(expected = BindingAlreadyExistsException.class)
    public void bindTest10() {
        Bindings bindings = Bindings.create();
        bindings.bind("x", int.class, 250);
        bindings.bind("x", String.class, "Hello world");
    }

    @Test
    public void bindTest11() {
        Bindings bindings = Bindings.create();
        Binding binding1 = BindingBuilder.with("x").value("Hello World!").build();
        bindings.bind(binding1);
        Binding binding2 = BindingBuilder.with("X").value(101).build();
        bindings.bind(binding2);

        Binding<String> match = bindings.getBinding("X");
        Assert.assertTrue(match.equals(binding2));
        match = bindings.getBinding("key");
        Assert.assertTrue(match == null);
    }

    @Test
    public void bindTest12() {
        Bindings bindings = Bindings.create();
        Binding binding1 = BindingBuilder.with("x").value("Hello World!").build();
        bindings.bind(binding1);
        Binding binding2 = BindingBuilder.with("X").value(101).build();
        bindings.bind(binding2);
        Binding binding3 = BindingBuilder.with("y").type(new TypeReference<Map<List<Integer>, String>>() {
        }).build();
        bindings.bind(binding3);

        Assert.assertTrue(bindings.contains("x"));
        Assert.assertTrue(bindings.contains("X"));
        Assert.assertTrue(bindings.contains("X", Integer.class));
        Assert.assertTrue(bindings.contains("x", String.class));
        Assert.assertTrue(bindings.contains("y", new TypeReference<Map<List<Integer>, String>>() {
        }));
        Assert.assertTrue(!bindings.contains("a"));
    }

    @Test
    public void bindTest13() {
        Bindings bindings = Bindings.create();
        Binding binding1 = BindingBuilder.with("x").value("Hello World!").build();
        bindings.bind(binding1);
        Binding binding2 = BindingBuilder.with("X").value(101).build();
        bindings.bind(binding2);
        Binding binding3 = BindingBuilder.with("y").type(new TypeReference<Map<List<Integer>, String>>() {
        }).build();
        bindings.bind(binding3);

        Binding match1 = bindings.getBinding("x");
        Binding match2 = bindings.getBinding("X", Integer.class);
        Binding match3 = bindings.getBinding("y", new TypeReference<Map<List<Integer>, String>>() {
        });
        Binding match4 = bindings.getBinding("a");
        Binding match5 = bindings.getBinding("a", Integer.class);
        Binding match6 = bindings.getBinding("a", new TypeReference<Map<List<Integer>, String>>() {
        });
        Assert.assertTrue(match1.equals(binding1));
        Assert.assertTrue(match2.equals(binding2));
        Assert.assertTrue(match3.equals(binding3));
        Assert.assertTrue(match4 == null);
        Assert.assertTrue(match5 == null);
        Assert.assertTrue(match6 == null);
    }

    @Test
    public void bindTest14() {
        Bindings bindings = Bindings.create();
        Binding binding1 = BindingBuilder.with("x").value("Hello World!").build();
        bindings.bind(binding1);
        Binding binding2 = BindingBuilder.with("X").value("101").build();
        bindings.bind(binding2);
        Binding binding3 = BindingBuilder.with("y").type(new TypeReference<Map<List<Integer>, String>>() {
        }).build();
        bindings.bind(binding3);
        Binding binding4 = BindingBuilder.with("z").type(new TypeReference<Map<String, String>>() {
        }).build();
        bindings.bind(binding4);
        Binding binding5 = BindingBuilder.with("a").type(new TypeReference<ArrayList<Integer>>() {
        }).value(new ArrayList<>()).build();
        bindings.bind(binding5);

        Map<String, Binding<String>> matches1 = bindings.getBindings(String.class);
        Assert.assertTrue(matches1.containsValue(binding1));
        Assert.assertTrue(matches1.containsValue(binding2));
        Assert.assertTrue(!matches1.containsValue(binding3));

        Map<String, Binding<Map>> matches2 = bindings.getBindings(Map.class);
        Assert.assertTrue(matches2.containsValue(binding3));
        Map<String, Binding<Map<?, ?>>> matches3 = bindings.getBindings(new TypeReference<Map<?, ?>>() {
        });
        Assert.assertTrue(matches3.size() == 2 && matches3.containsValue(binding3) && matches3.containsValue(binding4));
        Map<String, Binding<Map<List<Integer>, String>>> matches4 = bindings.getBindings(new TypeReference<Map<List<Integer>, String>>() {
        });
        Assert.assertTrue(matches4.size() == 1 && matches4.containsValue(binding3));
        Map<String, Binding<List<Integer>>> matches5 = bindings.getBindings(new TypeReference<List<Integer>>() {
        });
        Assert.assertTrue(matches5.size() == 1 && matches5.containsValue(binding5));
    }

    @Test
    public void bindTest15() {
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);

        Bindings bindings = Bindings.create();
        bindings.bind("x", String.class, "Hello World!");
        bindings.bind("y", List.class, values);

        Assert.assertTrue(bindings.getValue("x").equals("Hello World!"));
        Assert.assertTrue(bindings.getValue("y").equals(values));
    }

    @Test(expected = NoSuchBindingException.class)
    public void bindTest16() {
        Bindings bindings = Bindings.create();
        bindings.getValue("x");
    }

    @Test
    public void bindTest17() {
        Bindings bindings = Bindings.create();
        bindings.bind("x", String.class, "Hello World!");
        Assert.assertTrue(bindings.getValue("x").equals("Hello World!"));
        bindings.setValue("x", "new value");
        Assert.assertTrue(bindings.getValue("x").equals("new value"));
        bindings.bind("y", List.class);
        bindings.setValue("y", new ArrayList<>());
        Assert.assertTrue(bindings.getValue("y").equals(new ArrayList<>()));
    }

    @Test(expected = InvalidBindingException.class)
    public void bindTest18() {
        Bindings bindings = Bindings.create();
        bindings.bind("x", String.class, "Hello World!");
        bindings.setValue("x", 123);
    }

    @Test
    public void bindTest19() {
        List<Integer> values = new ArrayList<>();
        values.add(1);
        values.add(2);
        values.add(3);

        Bindings bindings = Bindings.create();
        Binding binding1 = BindingBuilder.with("x").value("Hello World!").build();
        bindings.bind(binding1);
        Binding binding2 = BindingBuilder.with("X").value(101).build();
        bindings.bind(binding2);
        Binding binding3 = BindingBuilder.with("y").value(values).build();
        bindings.bind(binding3);
        Binding binding4 = BindingBuilder.with("z").build();
        bindings.bind(binding4);

        Map<String, ?> bindingsMap = bindings.asMap();
        Assert.assertTrue(bindings.size() == bindingsMap.size());

        for (Binding<?> binding : bindings) {
            Assert.assertTrue(Objects.equals(binding.getValue(), bindingsMap.get(binding.getName())));
        }
    }

    @Test
    public void bindTest20() {
        Bindings bindings = Bindings.create()
                .bind("x", int.class, 250)
                .bind("y", Integer.class, 100);

        Map<String, Binding<Integer>> matches = bindings.getBindings(Integer.class);
        Assert.assertTrue(matches.size() == 2);
    }

    @Test
    public void bindTest21() {
        Bindings bindings = Bindings.create()
                .bind("a", String.class)
                .bind("b", new TypeReference<Set<Integer>>() {})
                .bind("c", new TypeReference<List<Integer>>() {})
                .bind("d", new TypeReference<Map<?, Long>>() {});

        Map<String, Binding<List<?>>> matches = bindings.getBindings(new TypeReference<List<?>>() {});
        Assert.assertTrue(matches.size() == 1);
        Map<String, Binding<Collection<?>>> matches1 = bindings.getBindings(new TypeReference<Collection<?>>() {});
        Assert.assertTrue(matches1.size() == 2);
    }
}