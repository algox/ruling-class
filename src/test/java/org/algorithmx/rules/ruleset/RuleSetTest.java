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
package org.algorithmx.rules.ruleset;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class RuleSetTest {

    public RuleSetTest() {
        super();
    }

    @Test
    public void test1() {
        Bindings bindings = Bindings.defaultBindings()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));
        RuleFactory ruleFactory = RuleFactory.defaultFactory();

        Rule rule6 = ruleFactory.rule("testrule6", Condition.arg0(() -> true), "this test rule 6 ");

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

        Assert.assertTrue(rule3.isPass("", "hello", 20));
    }
}
