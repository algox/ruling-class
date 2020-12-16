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
import org.algorithmx.rules.core.rule.RuleViolations;
import org.algorithmx.rules.core.rule.Severity;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.function.Supplier;

/**
 * Not Null validation Rule. Check to make sure the value is not null.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Given value cannot be null.")
public class NotNullRule extends ValidationRule {

    private static final String ERROR_CODE      = "validators.notNullRule";
    private static final String DEFAULT_MESSAGE = "Value cannot be null.";

    private final Supplier<Object> supplier;

    public NotNullRule(Object value) {
        this(() -> value);
    }

    public NotNullRule(Supplier<Object> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    @Given
    public boolean isValid() {
        return supplier.get() != null;
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {
        errors.add(createRuleViolationBuilder().build(context));
    }
}
