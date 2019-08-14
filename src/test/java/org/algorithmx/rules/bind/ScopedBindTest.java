package org.algorithmx.rules.bind;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScopedBindTest {

    public ScopedBindTest() {
        super();
    }

    @Test
    public void testBind1() {
        Bindings bindings = Bindings.scopedBindings();
        bindings.bind("key1", String.class, "value");
        bindings.bind("key2", new TypeReference<List<?>>(){});
        bindings.bind("key3", BigDecimal.class);
        bindings.bind("key4", new TypeReference<Map<? extends List<?>, List<Integer>>>(){});

        Assert.assertTrue(bindings.contains("key1", String.class));
        Assert.assertTrue(bindings.contains("key2", new TypeReference<List<Integer>>(){}));
        Assert.assertTrue(bindings.contains("key3", BigDecimal.class));
        Assert.assertTrue(bindings.contains("key4", new TypeReference<Map<ArrayList<?>, List<Integer>>>(){}));
    }
}
