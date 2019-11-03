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
package org.algorithmx.rules.validation;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.RuleContextBuilder;
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

public class ValidationTest1 {

    private RuleFactory ruleFactory;
    private RuleEngine ruleEngine;

    public ValidationTest1() {
        super();
    }

    @Before
    public void init() {
        this.ruleFactory = RuleFactory.defaultFactory();
        this.ruleEngine = RuleEngine.defaultRuleEngine();
    }

    @Test
    public void test1() {
        RuleSet rules = ruleFactory.rules("RuleSet1", "Test Rule Set")
                .add(TestRule1.class)
                .add(TestRule2.class)
                .add(TestRule3.class)
                .add(TestRule4.class)
                .add(TestRule5.class);

        Bindings bindings = Bindings.defaultBindings()
                .bind("value", Integer.class, 1)
                .bind("errors", ValidationErrorContainer.class, new ValidationErrorContainer());

        ruleEngine.run(RuleContextBuilder.create().bindWith(bindings).build(), rules);
    }

    @Test
    public void test2() {
        RuleSet rules = ruleFactory.rules("RuleSet2", "Test Rule Set")
                .add(TestRule1.class)
                .add(TestRule2.class)
                .add(TestRule3.class)
                .add(TestRule4.class)
                .add(TestRule5.class);

        ruleEngine.run(RuleContextBuilder.create()
                .bindWith(value -> 75, errors -> new ValidationErrorContainer()).build(), rules);
    }

    @Test
    public void testNotNullRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("b",1)
                .bind("d",new Date())
                .bind("e", errors);

        RuleSet rules = ruleFactory.rules("RuleSet2", "Test Rule Set")
                .add(new NotNullRule("Error.100", () -> bindings.getBinding("b")))
                .add(new PastDateRule("Error.200", () -> bindings.getBinding("d")));

        ruleEngine.run(RuleContextBuilder.create()
                .bindWith(bindings).build(), rules);
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testFutureDateRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("d", new Date())
                .bind("e", errors);

        RuleSet rules = ruleFactory.rules("RuleSet", "Test Rule Set")
                .add(new FutureDateRule("Error.100", "d"));

        ruleEngine.run(RuleContextBuilder.create()
                .bindWith(bindings).build(), rules);
        System.err.println(errors);
    }

}
