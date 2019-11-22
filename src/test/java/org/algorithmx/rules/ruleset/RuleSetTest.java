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
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleContextBuilder;
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.algorithmx.rules.core.Actions.act0;
import static org.algorithmx.rules.core.Actions.act1;
import static org.algorithmx.rules.core.Conditions.cond0;
import static org.algorithmx.rules.core.Conditions.cond1;
import static org.algorithmx.rules.core.Conditions.cond2;
import static org.algorithmx.rules.core.Conditions.cond3;

public class RuleSetTest {

    private RuleFactory ruleFactory;
    private RuleEngine ruleEngine;

    public RuleSetTest() {
        super();
    }

    @Before
    public void init() {
        this.ruleFactory = RuleFactory.defaultFactory();
        this.ruleEngine = RuleEngine.defaultRuleEngine();
    }

    //@Test
    public void test1() {
        Bindings bindings = Bindings.defaultBindings()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));
        RuleFactory ruleFactory = RuleFactory.defaultFactory();

        Rule rule6 = ruleFactory.rule()
                .given(cond0(() -> true))
                .then(act0(() -> System.err.println("XXX End")))
                .build();

        RuleSet rules = ruleFactory.rules("RuleSet1", "Test Rule Set")
                .add("test", ruleFactory.rule()
                        .given(cond1((String y) -> y.equals("")))
                        .then(act1((String y) -> System.err.println(y)))
                        .build())
                .add(ruleFactory.rule()
                        .given(cond2((String a, BigDecimal x) -> x != null))
                        .then(act0(() -> System.err.println("XXX Hello")))
                        .build())
                .add("testrule3", ruleFactory.rule()
                        .given(cond3((String a, String b, Integer c) -> c == 20 && "hello".equals(b)))
                        .then(act0(() -> System.err.println("XXX oh yeah")))
                        .build())
                .add(rule6);

        Rule rule1 = rules.getRule("test");
        Rule rule3 = rules.getRule("testrule3");

        Assert.assertTrue(rule3.isPass("", "hello", 20));
    }

    @Test
    public void testBind12() {
        Bindings bindings = Bindings.defaultBindings()
                .bind("key1", String.class, "value")
                .bind("key2", String.class, "value");

        RuleSet rules = ruleFactory.rules("RuleSet1", "Test Rule Set")
                .add("test", ruleFactory.rule()
                        .given(cond1((String key1) -> key1.equals("")))
                        .then(act1((String key2) -> System.err.println(key2)))
                        .build());

        ruleEngine.run(RuleContextBuilder.create()
                .bindWith(bindings).build(), rules);
    }
}
