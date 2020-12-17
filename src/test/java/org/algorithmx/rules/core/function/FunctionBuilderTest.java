package org.algorithmx.rules.core.function;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class FunctionBuilderTest {

    public FunctionBuilderTest() {
        super();
    }

    @Test
    public void testNoArg() {
        Function<Boolean> function = FunctionBuilder
                .with(() -> true)
                .name("function0")
                .build();

        Assert.assertTrue(function.getMethodDefinition().getName().equals("function0"));
        Assert.assertTrue(function.getMethodDefinition().getParameterDefinitions().length == 0);
    }

    @Test
    public void test2Args() {
        Function<BigDecimal> function = FunctionBuilder
                .with((String x, BigDecimal value) -> value)
                .build();

        Assert.assertTrue(function.getMethodDefinition().getParameterDefinitions().length == 2);
        Assert.assertTrue(function.getMethodDefinition().getParameterDefinitions()[1].getName().equals("value"));
        Assert.assertTrue(function.getMethodDefinition().getParameterDefinitions()[1].getType().equals(BigDecimal.class));

        function.apply("123", new BigDecimal("10.00"));
    }
}
