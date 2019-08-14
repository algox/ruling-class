package org.algorithmx.rules.ruleset;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.junit.Assert;

import java.math.BigDecimal;

public class RuleSetTest {

    public RuleSetTest() {
        super();
    }

    //@Test
    public void test1() {
        Bindings bindings = Bindings.simpleBindings()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));
        RuleFactory ruleFactory = RuleFactory.defaultFactory();

        IdentifiableRule rule6 = ruleFactory.rule("testrule6", Condition.arg0(() -> true), "this test rule 6 ");

        RuleSet rules = ruleFactory.rules("RuleSet1", "Test Rule Set")
                .add("test", (String y) -> y.equals(""), "")
                .add("testrule2", (String a, BigDecimal x) -> x != null,
                        "This test is to make sure its working!")
                .add("testrule3", (String a, String b, Integer c) -> c == 20 && "hello".equals(b),
                "")
                .add("testrule4", () -> true, "")
                .add(rule6);

        Rule rule1 = rules.get("test");
        Rule rule2 = rules.get("testrule2");
        Rule rule3 = rules.get("testrule3");
        Rule rule4 = rules.get("testrule4");
        CompositeRule rule5 = ruleFactory.and(rules);

        Assert.assertTrue(rule3.isPass(bindings));
        Assert.assertTrue(rule3.isPass(RuleExecutionContext.create(bindings)));

        Assert.assertTrue(rule1.or(rule2).and(rule3).isPass(bindings));
        Assert.assertTrue(rule1.and(rule3).isPass(bindings));
        Assert.assertTrue(rule1.or(rule3).isPass(bindings));
        Assert.assertTrue(rule4.negate().negate().isPass(bindings));
        Assert.assertTrue(rule5.isPass(bindings));
    }
}
