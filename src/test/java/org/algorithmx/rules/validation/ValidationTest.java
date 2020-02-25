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

import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.build.RuleContextBuilder;
import org.algorithmx.rules.build.RuleSetBuilder;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.model.Severity;
import org.junit.Assert;
import org.junit.Test;

import java.util.Date;

/**
 * Test cases covering the various Validation Rules.
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

        Bindings bindings = Bindings.defaultBindings()
                .bind("value", Integer.class, 1)
                .bind("errors", ValidationErrorContainer.class, new ValidationErrorContainer());

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

        rules.run(RuleContextBuilder.with(value -> 75, errors -> new ValidationErrorContainer()).build());
    }

    @Test
    public void testNotNullRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("b",1)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule(new NotNullRule("Error.100", () -> bindings.getBinding("b")))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testNullRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("b", String.class, null)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule(new NullRule("Error.100", () -> bindings.getBinding("b")))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testStringLengthRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("b", String.class, "  ")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule(new StringHasLengthRule("Error.100", () -> bindings.getBinding("b")))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testStringTextRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("b", String.class, "  a")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule(new StringHasTextRule("Error.100", () -> bindings.getBinding("b")))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 0);
    }

    @Test
    public void testStringPatternRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("b", String.class, "ababab")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet2", "Test Rule Set")
                .rule(new PatternMatchRule("[z]*", "Error.100", () -> bindings.getBinding("b")))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
        Assert.assertTrue(errors.size() == 1);
    }


    @Test
    public void testFutureDateRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("d", new Date())
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule(new FutureDateRule("Error.100", "d"))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());
    }

    @Test
    public void tesPastDateRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("d", new Date())
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule(new PastDateRule("Error.100", "d"))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());

        Assert.assertTrue(errors.size() == 0);
    }


    @Test
    public void testMaxRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("a", 25)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule(new MaxRule(50, "Error.100", "a"))
                .rule(new MaxRule(20, "Error.101", "a"))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());

        Assert.assertTrue(errors.size() == 1);
    }

    @Test
    public void testMinRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("a", 10)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule(new MinRule(11, "Error.100", "a"))
                .rule(new MinRule(5, "Error.101", "a"))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());

        Assert.assertTrue(errors.size() == 1);
    }

    @Test
    public void testMaxMinRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("a", 22)
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule(new RangeRule(1, 10, "Error.100", "a"))
                .rule(new RangeRule(20, 25, "Error.101", "a"))
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());

        Assert.assertTrue(errors.size() == 1);
    }

    @Test
    public void testValidationRule() {
        ValidationErrorContainer errors = new ValidationErrorContainer();
        Bindings bindings = Bindings.defaultBindings()
                .bind("x", 22)
                .bind("z", "abcde")
                .bind("e", errors);

        RuleSet rules = RuleSetBuilder.with("RuleSet", "Test Rule Set")
                .rule(TestValidationRule.class)
                .build();

        rules.run(RuleContextBuilder.with(bindings).build());

        Assert.assertTrue(errors.size() == 1);
    }

    @Rule
    public static class TestValidationRule extends ValidationRule {

        public TestValidationRule() {
            super("Error.101", Severity.ERROR, "Invalid input ${x} ${z}");
        }

        @Given
        public boolean when(Integer x, String z) {
            return x != null && x > 100;
        }
    }
}
