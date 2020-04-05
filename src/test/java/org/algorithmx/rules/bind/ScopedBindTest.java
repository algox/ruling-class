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
import java.util.Map;

/**
 * Tests for Scoped Bindings.
 *
 * @author Max Arulananthan
 */
public class ScopedBindTest {

    public ScopedBindTest() {
        super();
    }

    @Test
    public void testBind1() {
        Bindings bindings = Bindings.create()
                .bind("key1", String.class, "value")
                .bind("key2", new TypeReference<List<?>>() {})
                .bind("key3", BigDecimal.class)
                .bind("key4", new TypeReference<Map<? extends List<?>, List<Integer>>>() {});

        Assert.assertTrue(bindings.contains("key1", String.class));
        Assert.assertTrue(bindings.contains("key2", new TypeReference<List<Integer>>() {}));
        Assert.assertTrue(bindings.contains("key3", BigDecimal.class));
        Assert.assertTrue(bindings.contains("key4", new TypeReference<Map<ArrayList<?>, List<Integer>>>() {}));
        Assert.assertTrue(bindings.size() == 4);
    }

    @Test
    public void testBind2() {
        ScopedBindings bindings = Bindings.create()
                .bind("key1", String.class, "value");

        Assert.assertTrue(bindings.get("key1").equals("value"));
        bindings.newScope();
        bindings.bind(BindingBuilder.with("key1").type(String.class).value("value2").build());
        Assert.assertTrue(bindings.size() == 2);
        bindings.endScope();
        Assert.assertTrue(bindings.get("key1").equals("value"));
        Assert.assertTrue(bindings.size() == 1);


    }
}
