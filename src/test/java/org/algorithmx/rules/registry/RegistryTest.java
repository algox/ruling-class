package org.algorithmx.rules.registry;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class RegistryTest {

    public RegistryTest() {
        super();
    }

    @Test
    public void test1() {
        RuleSet rules = RuleSet.create("RuleSet1");

        Rule rule1 = rules.add("test", (String y) -> y.equals(""), "");
        Rule rule2 = rules.add("testrule2", (String a, BigDecimal x) -> x != null,
                        "This test is to make sure its working!");
        Rule rule3 = rules.add("testrule3", (String a, String b, Integer c) -> c == 20 && "hello".equals(b),
                "");
        Rule rule4 = rules.add("testrule4", () -> false, "");

        Assert.assertTrue(rule1.run(""));
        Assert.assertTrue(rule3.run("", "hello", 20));

        Bindings bindings = Bindings.create()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));

        Assert.assertTrue(rule3.test(bindings));
        Assert.assertTrue(rule3.test(RuleExecutionContext.create(bindings)));

        Assert.assertTrue(rule1.or(rule2).test(RuleExecutionContext.create(bindings)));
        Assert.assertTrue(rule1.and(rule3).test(RuleExecutionContext.create(bindings)));
        Assert.assertTrue(rule1.or(rule3).test(RuleExecutionContext.create(bindings)));
        Assert.assertTrue(rule4.negate().test(RuleExecutionContext.create(bindings)));
    }
}
