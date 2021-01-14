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
package org.algorithmx.rules.validation.rules.binding;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.ValidationRule;

/**
 * Validation Rule to make sure the the given BindingName is NOT defined.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Binding Name must NOT exist.")
public class MustNotBeDefinedRule extends ValidationRule {

    private static final String ERROR_CODE      = "rulii.validation.rules.MustNotBeDefinedRule.errorCode";
    private static final String DEFAULT_MESSAGE = "Binding {0} must not be defined.";

    private final String bindingName;

    public MustNotBeDefinedRule(String bindingName) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(bindingName, "bindingName cannot be null.");
        this.bindingName = bindingName;
    }

    @Given
    public boolean isValid(RuleContext context) {
        return !context.getBindings().contains(bindingName);
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("bindingName",bindingName);

        errors.add(builder.build(context));
    }

    @Override
    public String toString() {
        return "MustNotBeDefinedRule{" +
                "bindingName=" + bindingName +
                '}';
    }
}