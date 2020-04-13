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
import org.algorithmx.rules.bind.BindingBuilder;
import org.algorithmx.rules.bind.BindingException;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ScopedBindings;
import org.algorithmx.rules.bind.convert.string.ConverterRegistry;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.util.TypeReference;
import org.algorithmx.rules.util.reflect.ObjectFactory;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * Parameter Resolver tests.
 *
 * @author Max Arulananthan
 */

public class ParameterResolverTest {

    public ParameterResolverTest() {
        super();
    }

    @Test
    public void matchByNameTest1() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod1"));

        Bindings bindings = Bindings.create()
                .bind("a", String.class, "Hello World!")
                .bind("b", new TypeReference<Set<Integer>>() {}, new HashSet<>())
                .bind("c", new TypeReference<List<Integer>>() {}, new ArrayList<>())
                .bind("d", new TypeReference<Map<?, Long>>() {}, new HashMap<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 4);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("c") && matches[2].isBinding());
        Assert.assertTrue(matches[3].getBinding().getName().equals("d") && !matches[3].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("a")));
        Assert.assertTrue(values[1].equals(bindings.getValue("b")));
        Assert.assertTrue(values[2].equals(bindings.getBinding("c")));
        Assert.assertTrue(values[3].equals(bindings.getValue("d")));
    }

    @Test
    public void matchByNameTest2() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod2"));

        Bindings bindings = Bindings.create()
                .bind("a", int.class, 10)
                .bind("values", new TypeReference<Map<String, Integer>>() {}, new HashMap<>())
                .bind("c", new TypeReference<List<Integer>>() {}, new ArrayList<>())
                .bind("d", new TypeReference<Map<?, Long>>() {}, new HashMap<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 2);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("values") && !matches[1].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0],
                bindings,BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("a")));
        Assert.assertTrue(values[1].equals(bindings.getValue("values")));
    }

    @Test
    public void matchByNameTest3() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod3"));

        Bindings bindings = Bindings.create()
                .bind("x", int.class, 255)
                .bind("b", new TypeReference<List<Integer>>() {}, new ArrayList<>())
                .bind("c", new TypeReference<Map<?, Long>>() {}, new HashMap<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 1);
        Assert.assertTrue(matches[0].getBinding().getName().equals("x") && matches[0].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getBinding("x")));
    }

    @Test
    public void matchByNameTest4() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod4"));

        Bindings bindings = Bindings.create()
                .bind("a", String.class, "Hello world!")
                .bind("b", int.class, 1)
                .bind("x", new TypeReference<List<Integer>>() {}, new ArrayList<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 3);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("x") && matches[2].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("a")));
        Assert.assertTrue(values[1].equals(bindings.getValue("b")));
        Assert.assertTrue(values[2].equals(bindings.getBinding("x")));
    }

    @Test
    public void matchByTypeTest1() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod1"));

        Bindings bindings = Bindings.create()
                .bind("a", String.class, "Ruling class")
                .bind("b", new TypeReference<Set<Integer>>() {}, new HashSet<>())
                .bind("c", new TypeReference<List<Integer>>() {}, new ArrayList<>())
                .bind("d", new TypeReference<Map<String, Long>>() {}, new HashMap<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 4);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("c") && matches[2].isBinding());
        Assert.assertTrue(matches[3].getBinding().getName().equals("d") && !matches[3].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("a")));
        Assert.assertTrue(values[1].equals(bindings.getValue("b")));
        Assert.assertTrue(values[2].equals(bindings.getBinding("c")));
        Assert.assertTrue(values[3].equals(bindings.getValue("d")));
    }

    @Test
    public void matchByTypeTest2() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod2"));

        ScopedBindings bindings = Bindings.createWithScopes()
                .bind("key1", Integer.class, 23);

        bindings.addScope()
                .bind("key2", new TypeReference<Map<String, Integer>>() {}, new HashMap<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 2);
        Assert.assertTrue(matches[0].getBinding().getName().equals("key1") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("key2") && !matches[1].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("key1")));
        Assert.assertTrue(values[1].equals(bindings.getValue("key2")));
    }

    @Test(expected = BindingException.class)
    public void matchByTypeTest3() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod3"));

        ScopedBindings bindings = Bindings.createWithScopes()
                .bind("key1", String.class, "Ruling class")
                .bind("key2", Integer.class, 24);

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 1);
        Assert.assertTrue(matches[0].getBinding().getName().equals("key1") && matches[0].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("key1")));
    }

    @Test(expected = BindingException.class)
    public void matchByTypeTest4() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod4"));

        ScopedBindings bindings = Bindings.createWithScopes()
                .bind(BindingBuilder.with("key1").value("test").build());
        bindings.addScope()
                .bind("key3", new TypeReference<List<Integer>>() {}, new ArrayList<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 3);
        Assert.assertTrue(matches[0].getBinding().getName().equals("key1") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding() == null);
        Assert.assertTrue(matches[2].getBinding().getName().equals("key3") && matches[2].isBinding());

        resolver.resolve(matches, definitions[0], bindings, BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(),
                ConverterRegistry.create());
    }

    @Test
    public void matchByNameAndTypeTest1() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod1"));

        Bindings bindings = Bindings.create()
                .bind("a", String.class, "Ruling class")
                .bind("b", new TypeReference<Set<Integer>>() {}, new HashSet<>())
                .bind("c", new TypeReference<List<Integer>>() {}, new Vector())
                .bind("d", new TypeReference<Map<String, Long>>() {}, new HashMap<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 4);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a") && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("c") && matches[2].isBinding());
        Assert.assertTrue(matches[3].getBinding().getName().equals("d") && !matches[3].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_TYPE.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[0].equals(bindings.getValue("a")));
        Assert.assertTrue(values[1].equals(bindings.getValue("b")));
        Assert.assertTrue(values[2].equals(bindings.getBinding("c")));
        Assert.assertTrue(values[3].equals(bindings.getValue("d")));
    }

    @Test
    public void matchByNameAndTypeTest2() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod4"));

        ScopedBindings bindings = ScopedBindings.create()
                .bind("a", String.class, "Ruling class")
                .bind("b", Integer.class, 8);
        bindings.addScope()
                .bind("a", Integer.class, 50)
                .bind("x", new TypeReference<List<Integer>>() {}, new ArrayList<>());

        Assert.assertTrue(definitions.length == 1);
        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 3);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a")
                && matches[0].getBinding().getType().equals(String.class) && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("x") && matches[2].isBinding());

        Object[] values = resolver.resolve(matches, definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE.getStrategy(), ConverterRegistry.create());
        Assert.assertTrue(values[2].equals(bindings.getBinding("x")));
        bindings.removeScope();
        Assert.assertTrue(values[0].equals(bindings.getValue("a")));
        Assert.assertTrue(values[1].equals(bindings.getValue("b")));

    }

    @Test
    public void matchByNameThenByTypeTest() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod4"));

        ScopedBindings bindings = ScopedBindings.create()
                .bind("a", String.class, "Ruling class")
                .bind("b", Integer.class, 1)
                .bind("c", new TypeReference<List<Integer>>() {}, new ArrayList<>());
        bindings.addScope()
                .bind("a", Integer.class, 33)
                .bind("q", new TypeReference<Set<Integer>>() {}, new HashSet<>());

        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_THEN_BY_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 3);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a")
                && matches[0].getBinding().getType().equals(Integer.class) && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("c") && matches[2].isBinding());
    }

    @Test
    public void matchByNameAndTypeThenByTypeTest() {
        ParameterResolver resolver = ParameterResolver.create();
        MethodDefinition[] definitions = MethodDefinition.load(TestClass.class, method -> method.getName().equals("testMethod4"));

        ScopedBindings bindings = ScopedBindings.create()
                .bind("a", String.class, "Ruling class")
                .bind("b", Integer.class, 34)
                .bind("c", new TypeReference<List<Integer>>() {}, new ArrayList<>());
        bindings.addScope()
                .bind("a", Integer.class, 3)
                .bind("x", new TypeReference<Set<Integer>>() {}, new HashSet<>());

        ParameterMatch[] matches = resolver.match(definitions[0], bindings,
                BindingMatchingStrategyType.MATCH_BY_NAME_AND_TYPE_THEN_BY_JUST_BY_TYPE.getStrategy(), ObjectFactory.create());

        Assert.assertTrue(matches.length == 3);
        Assert.assertTrue(matches[0].getBinding().getName().equals("a")
                && matches[0].getBinding().getType().equals(String.class) && !matches[0].isBinding());
        Assert.assertTrue(matches[1].getBinding().getName().equals("b") && !matches[1].isBinding());
        Assert.assertTrue(matches[2].getBinding().getName().equals("c") && matches[2].isBinding());
    }

    private static class TestClass {

        public boolean testMethod1(String a, Set<Integer> b, Binding<List<Integer>> c, Map<?, Long> d) {
            return true;
        }

        public void testMethod2(int a, Map<String, Integer> values) {}

        public boolean testMethod3(Binding<?> x) {
            return true;
        }

        public boolean testMethod4(String a, Integer b, Binding<List<Integer>> x) {
            return true;
        }

    }
}
