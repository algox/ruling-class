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
                .add(ruleFactory.rule("test", Condition.arg1((String y) -> y.equals("")))
                        .then(Action.arg1((String y) -> System.err.println(y))))
                .add(ruleFactory.rule("testrule3", Condition.arg2((String a, BigDecimal x) -> x != null), "This test is to make sure its working!")
                                .then(Action.arg0(() -> System.err.println("XXX Hello"))))
                .add(ruleFactory.rule(Condition.arg3((String a, String b, Integer c) -> c == 20 && "hello".equals(b))).then(Action.arg0(() -> System.err.println("XXX oh yeah"))))
                .add(rule6.then(Action.arg0(() -> System.err.println("XXX End"))));

        Rule rule1 = rules.getRule("test");
        Rule rule3 = rules.getRule("testrule3");
        // TODO : Fix
        //CompositeRule rule5 = ruleFactory.and(rules);

        Assert.assertTrue(rule3.isPass(bindings));
        Assert.assertTrue(rule3.isPass(RuleExecutionContext.create(bindings)));

        Assert.assertTrue(rule1.or(rule3).and(rule3).isPass(bindings));
        Assert.assertTrue(rule1.and(rule3).isPass(bindings));
        Assert.assertTrue(rule1.or(rule3).isPass(bindings));
    }
}
