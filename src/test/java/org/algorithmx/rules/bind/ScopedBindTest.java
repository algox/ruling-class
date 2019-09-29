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

/**
 * Basic Scoped Binding tests.
 *
 * @author Max Arulananthan
 */
public class ScopedBindTest {

    public ScopedBindTest() {
        super();
    }

    @Test
    public void testBind1() {
        Bindings bindings = Bindings.defaultBindings();
        bindings.bind("key1", String.class, "value");
        bindings.bind("key2", new TypeReference<List<?>>() {
        });
        bindings.bind("key3", BigDecimal.class);
        bindings.bind("key4", new TypeReference<Map<? extends List<?>, List<Integer>>>() {
        });

        Assert.assertTrue(bindings.contains("key1", String.class));
        Assert.assertTrue(bindings.contains("key2", new TypeReference<List<Integer>>() {
        }));
        Assert.assertTrue(bindings.contains("key3", BigDecimal.class));
        Assert.assertTrue(bindings.contains("key4", new TypeReference<Map<ArrayList<?>, List<Integer>>>() {
        }));
        Assert.assertTrue(bindings.size() == 5);
    }

    @Test
    public void testBind2() {
        ScopedBindings bindings = Bindings.defaultBindings(false);
        bindings.bind("key1", String.class, "value");
        Assert.assertTrue(bindings.get("key1").equals("value"));
        bindings.newScope();
        bindings.bind("key1", String.class, "value2");
        Assert.assertTrue(bindings.size() == 2);
        bindings.endScope();
        Assert.assertTrue(bindings.get("key1").equals("value"));
        Assert.assertTrue(bindings.size() == 1);
    }
}
