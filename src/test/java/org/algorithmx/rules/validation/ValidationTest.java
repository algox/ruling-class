/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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

import org.algorithmx.rules.annotation.Optional;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.function.FunctionBuilder;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleContextBuilder;
import org.algorithmx.rules.core.rule.RuleViolations;
import org.algorithmx.rules.core.ruleset.RuleResultSet;
import org.algorithmx.rules.core.ruleset.RuleSet;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;
import org.algorithmx.rules.trace.ConsoleLogger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Test cases for the various Validation Rules.
 *
 * @author Max Arulananthan.
 */
public class ValidationTest {

    public ValidationTest() {
        super();
    }

    @Test
    public void test1() {
        RuleSet rules = RuleSetBuilder.with("RuleSet1", "Test Rule Set")
                .rule(TestRule1.class)
                .rule(TestRule2.class)
                .rule(TestRule3.class)
                .rule(TestRule4.class)
                .rule(TestRule5.class).build();

        Bindings bindings = Bindings.create()
                .bind("value", Integer.class, 1)
                .bind("errors", RuleViolations.class, new RuleViolations());

        rules.run(RuleContextBuilder.with(bindings).build());
    }

    @Test
    public void test2() {
        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule(TestRule1.class)
                .rule(TestRule2.class)
                .rule(TestRule3.class)
                .rule(TestRule4.class)
                .rule(TestRule5.class)
                .build();

        rules.run(RuleContextBuilder.
                with(Bindings.create()
                        .bind(value -> 75)
                        .bind(errors -> new RuleViolations())).build());
    }

    @Test
    public void testNotNullRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("b",1)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule((Object b) -> new NotNullRule(b))
                .build();

        RuleContext ctx = RuleContextBuilder.with(bindings).build();
        rules.run(ctx);
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testNullRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("b", String.class, null)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule((Object b) -> new NullRule(b))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testStringLengthRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("b", String.class, "  ")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule((String b) -> new StringHasLengthRule(b))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testStringTextRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("b", String.class, "  a")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule((String b) -> new StringHasTextRule(b))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testStringPatternRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("b", String.class, "ababab")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule((String b) -> new RegexPatternMatchRule("[z]*", b))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 1);
    }


    @Test
    public void testFutureDateRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("d", new Date())
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((Date d) -> new FutureDateRule(d))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
    }

    @Test
    public void tesPastDateRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("d", new Date())
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((Date d) -> new PastDateRule(d))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }


    @Test
    public void testMaxRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("a", 25)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((Integer a) -> new MaxRule(50, a))
                .rule((Integer a) -> new MaxRule(20, a))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 1);
    }

    @Test
    public void testMinRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("a", 10)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((Integer a) -> new MinRule(11, a))
                .rule((Integer a) -> new MinRule(5, a))
                .rule(new MinRule(25, 22).defaultMessage("test ${value} ${min}"))
                .rule(FunctionBuilder.with((Integer a) -> new MinRule(25, a)).build())
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 3);
    }

    @Test
    public void testRangeRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("a", 22)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((Integer a) -> new RangeRule(1, 10, a))
                .rule((Integer a) -> new RangeRule(20, 25, a))
                .preAction(ActionBuilder.create(() -> System.err.println("Pre Action")))
                .postAction(ActionBuilder.create(() -> System.err.println("Post Action")))
                .stopWhen(ConditionBuilder.create((RuleViolations e) -> e.hasErrors()))
                .build();

        RuleContext ctx = RuleContextBuilder
                .with(bindings)
                .traceUsing(new ConsoleLogger(true))
                .build();
        RuleResultSet result = rules.run(ctx);
        Assert.assertTrue(errors.size() == 1);
    }

    @Test
    public void testMustBeDefinedRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("a", 22)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((@Optional Binding<Integer> a) -> new MustBeDefinedRule(a))
                .rule((@Optional Binding<Integer> c) -> new MustBeDefinedRule(c))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 1);
    }

    @Test
    public void testMustNotBeDefinedRule() {
        RuleViolations errors = new RuleViolations();
        Bindings bindings = Bindings.create()
                .bind("a", 22)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule((@Optional Binding<Integer> a) -> new MustNotBeDefinedRule(a))
                .rule((@Optional Binding<Integer> c) -> new MustNotBeDefinedRule(c))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 1);
    }
}
