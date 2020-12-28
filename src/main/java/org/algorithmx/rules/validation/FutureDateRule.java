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

import java.util.Date;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the Date is in the future.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Date must be in the future.")
public class FutureDateRule extends ValidationRule {

    private static final String ERROR_CODE      = "validators.futureDateRule";
    private static final String DEFAULT_MESSAGE = "Date must be in the future. Given {0}. Current Date {1}.";

    private final Supplier<Date> supplier;

    public FutureDateRule(Date value) {
        this(() -> value);
    }

    public FutureDateRule(Supplier<Date> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    /**
     * Determines if the given date is in the future.
     *
     * @return true if the given date is in the future; false otherwise.
     */
    @Given
    public boolean isValid() {
        Date value = supplier.get();
        if (value == null) return false;
        Date currentDate = new Date();
        return value != null && currentDate.before(value);
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("value", supplier.get())
                .param("currentDate", new Date());

        errors.add(builder.build(context));
    }

    @Override
    public String toString() {
        return "FutureDateRule{" +
                "value=" + supplier.get() +
                '}';
    }
}
