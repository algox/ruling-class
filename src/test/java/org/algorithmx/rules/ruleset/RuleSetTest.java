package org.algorithmx.rules.ruleset;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class RuleSetTest {

    public RuleSetTest() {
        super();
    }

    @Test
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
                .add("test", ruleFactory.rule((String y) -> y.equals(""))
                        .then((String y) -> System.err.println(y)))
                .add(ruleFactory.rule((String a, BigDecimal x) -> x != null)
                                .then(() -> System.err.println("XXX Hello")))
                .add("testrule3", ruleFactory.rule((String a, String b, Integer c) -> c == 20 && "hello".equals(b))
                        .then(() -> System.err.println("XXX oh yeah")))
                .add(rule6.then(() -> System.err.println("XXX End")));

        Rule rule1 = rules.getRule("test");
        Rule rule3 = rules.getRule("testrule3");
        CompositeRule rule5 = ruleFactory.and(rules);

        Assert.assertTrue(rule3.isPass(bindings));
        Assert.assertTrue(rule3.isPass(RuleExecutionContext.create(bindings)));

        Assert.assertTrue(rule1.or(rule3).and(rule3).isPass(bindings));
        Assert.assertTrue(rule1.and(rule3).isPass(bindings));
        Assert.assertTrue(rule1.or(rule3).isPass(bindings));
    }
}
