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

package org.algorithmx.rulii.test.extract;

import org.algorithmx.rulii.extract.DefaultExtractorRegistry;
import org.algorithmx.rulii.extract.ExtractorRegistry;
import org.algorithmx.rulii.extract.TypedValueExtractor;
import org.algorithmx.rulii.extract.TypedValueProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class TypedValueExtractorTest {

    private final ExtractorRegistry registry = new DefaultExtractorRegistry(true);

    public TypedValueExtractorTest() {
        super();
    }

    @Test
    public void test1() {
        AtomicReference<String> value = new AtomicReference<>("value1");
        TypedValueExtractor extractor = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor);
        Collector collector = new Collector();
        extractor.extract(value, collector);
        Assert.assertTrue(collector.getValues()[0].equals("value1"));
    }

    @Test
    public void test2() {
        List<Integer> value = Arrays.asList(1,2,3,4,5);
        TypedValueExtractor extractor = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor);
        Collector collector = new Collector();
        extractor.extract(value, collector);
        Assert.assertTrue(collector.getValues()[0].equals(1));
        Assert.assertTrue(collector.getValues()[1].equals(2));
        Assert.assertTrue(collector.getValues()[2].equals(3));
        Assert.assertTrue(collector.getValues()[3].equals(4));
        Assert.assertTrue(collector.getValues()[4].equals(5));
    }

    @Test
    public void test3() {
        Set<String> value = new HashSet<>(Arrays.asList("a", "b", "c", "d", "e"));
        TypedValueExtractor extractor = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor);
        Collector collector = new Collector();
        extractor.extract(value, collector);
        Set<Object> valueSet = new HashSet<>(collector.values);
        Assert.assertTrue(valueSet.contains("a"));
        Assert.assertTrue(valueSet.contains("b"));
        Assert.assertTrue(valueSet.contains("c"));
        Assert.assertTrue(valueSet.contains("d"));
        Assert.assertTrue(valueSet.contains("e"));
    }

    @Test
    public void test4() {
        Map<String,Integer> value = new HashMap<>();
        value.put("a", 1);
        value.put("b", 2);
        value.put("c", 3);
        value.put("d", 4);
        value.put("e", 5);
        TypedValueExtractor extractor1 = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor1);
        Collector collector = new Collector();
        extractor1.extract(value, collector);
        Set<Object> valueSet = new HashSet<>(collector.values);
        Assert.assertTrue(valueSet.contains("a"));
        Assert.assertTrue(valueSet.contains("b"));
        Assert.assertTrue(valueSet.contains("c"));
        Assert.assertTrue(valueSet.contains("d"));
        Assert.assertTrue(valueSet.contains("e"));

        TypedValueExtractor extractor2 = registry.find(value.getClass(), 1);
        Assert.assertNotNull(extractor2);
        extractor2.extract(value, collector);
        valueSet = new HashSet<>(collector.values);
        Assert.assertTrue(valueSet.contains(1));
        Assert.assertTrue(valueSet.contains(2));
        Assert.assertTrue(valueSet.contains(3));
        Assert.assertTrue(valueSet.contains(4));
        Assert.assertTrue(valueSet.contains(5));
    }

    @Test
    public void test5() {
        Optional<String> value = Optional.of("value");
        TypedValueExtractor extractor = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor);
        Collector collector = new Collector();
        extractor.extract(value, collector);
        Assert.assertTrue(collector.getValues()[0].equals("value"));
    }

    @Test
    public void test6() {
        ThreadLocal<String> value = new ThreadLocal();
        value.set("value");
        TypedValueExtractor extractor = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor);
        Collector collector = new Collector();
        extractor.extract(value, collector);
        Assert.assertTrue(collector.getValues()[0].equals("value"));
    }

    @Test
    public void test7() {
        Reference<String> value = new WeakReference<>("value");
        TypedValueExtractor extractor = registry.find(value.getClass(), 0);
        Assert.assertNotNull(extractor);
        Collector collector = new Collector();
        extractor.extract(value, collector);
        Assert.assertTrue(collector.getValues()[0].equals("value"));
    }

    private static class Collector implements TypedValueProcessor {
        private List<Object> values = new ArrayList<>();

        public Collector() {
            super();
        }

        @Override
        public void value(String name, Object value) {
            values.add(value);
        }

        @Override
        public void indexedValue(String name, int index, Object value) {
            values.add(value);
        }

        public Object[] getValues() {
            return values.toArray();
        }
    }
}
