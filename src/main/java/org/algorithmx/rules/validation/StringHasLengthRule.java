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
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleViolationBuilder;
import org.algorithmx.rules.core.rule.RuleViolations;
import org.algorithmx.rules.core.rule.Severity;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.function.Supplier;

/**
 * Validation Rule to make sure the String value has length.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value has length.")
public class StringHasLengthRule extends ValidationRule {

    private static final String ERROR_CODE      = "validators.stringHasLengthRule";
    private static final String DEFAULT_MESSAGE = "Value cannot be empty. Given {0}.";

    private final Supplier<String> supplier;

    public StringHasLengthRule(String value) {
        this(() -> value);
    }

    public StringHasLengthRule(Supplier<String> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    /**
     * Determines whether the given text is not empty.
     *
     * @return true if not empty; false otherwise.
     */
    @Given
    public boolean isValid() {
        String value = supplier.get();
        if (value == null) return false;
        return value.length() > 0;
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("value", supplier.get());

        errors.add(builder.build(context));
    }
}
