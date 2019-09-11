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
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.model.ValidationErrorContainer;
import org.junit.Before;
import org.junit.Test;

import static org.algorithmx.rules.core.Condition.*;

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

        ruleEngine.run(rules, bindings);
        System.err.println(bindings.get("errors").toString());
    }

    @Test
    public void test2() {
        RuleSet rules = ruleFactory.rules("RuleSet2", "Test Rule Set")
                .add(TestRule1.class)
                .add(TestRule2.class)
                .add(TestRule3.class)
                .add(TestRule4.class)
                .add(TestRule5.class);

        RuleExecutionContext ctx = ruleEngine.run(rules, value -> 75, errors -> new ValidationErrorContainer());
        System.err.println(ctx.bindings().get("errors").toString());

        cond3((String a, String b, Integer c) -> c > 10);
    }
}
