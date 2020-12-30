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

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Validation Rule to make sure the String value matches the given regex pattern.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value matches the given regex pattern.")
public class RegexPatternMatchRule extends ValidationRule {

    private static final String ERROR_CODE      = "validators.regexPatternMatchRule";
    private static final String DEFAULT_MESSAGE = "Value must match Regex pattern {1}. Given {0}.";

    private final Pattern pattern;
    private final Supplier<String> supplier;

    public RegexPatternMatchRule(String pattern, String value) {
        this(pattern, () -> value);
    }

    public RegexPatternMatchRule(String pattern, Supplier<String> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(pattern, "pattern cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.pattern = Pattern.compile(pattern);
        this.supplier = supplier;
    }

    @Given
    public boolean isValid() {
        String value = supplier.get();
        if (value == null) return false;
        return matches(value, pattern);
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("value", supplier.get())
                .param("pattern", pattern.pattern());

        errors.add(builder.build(context));
    }

    public Pattern getPattern() {
        return pattern;
    }

    /**
     * Determines whether the given value matches the desired regex pattern.
     *
     * @param value text value.
     * @param pattern desired pattern.
     * @return true if the text matches the pattern; false otherwise.
     */
    private static boolean matches(String value, Pattern pattern) {
        return value != null && pattern.matcher(value).matches();
    }

    @Override
    public String toString() {
        return "RegexPatternMatchRule{" +
                "pattern=" + pattern +
                ", value=" + supplier.get() +
                '}';
    }
}
