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

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;

import java.util.function.Function;

/**
 * Validation Rule based on the given Function.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Validation Rule based on the given Function.")
public class ValidationRule<T> extends ValidationRuleTemplate<T> {

    private final Function<Binding<T>, Boolean> condition;

    /**
     * Ctor taking in the error, rule name, binding name, and the validating function.
     *
     * @param error validation error.
     * @param ruleName rule name to make sure its unique (in a RuleSet).
     * @param bindingName name of the Binding.
     * @param condition validating function.
     */
    public ValidationRule(ValidationError error, String ruleName, String bindingName, Function<Binding<T>, Boolean> condition) {
        super(error, ruleName, bindingName);
        this.condition = condition;
    }

    /**
     * Ctor taking in the rule name, binding name, error code, severity, error message and the validating function.
     *
     * @param ruleName rule name to make sure its unique (in a RuleSet).
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     * @param condition validating function.
     */
    public ValidationRule(String ruleName, String bindingName, String errorCode, Function<Binding<T>, Boolean> condition) {
        super(ruleName, bindingName, errorCode, Severity.FATAL, null);
        this.condition = condition;
    }

    /**
     * Ctor taking in the rule name, binding name, error code, severity, error message and the validating function.
     *
     * @param ruleName rule name to make sure its unique (in a RuleSet).
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     * @param errorMessage error message to be returned.
     * @param condition validating function.
     */
    public ValidationRule(String ruleName, String bindingName, String errorCode,
                          String errorMessage, Function<Binding<T>, Boolean> condition) {
        super(ruleName, bindingName, errorCode, Severity.FATAL, errorMessage);
        this.condition = condition;
    }

    /**
     * Ctor taking in the rule name, binding name, error code, severity, error message and the validating function.
     *
     * @param ruleName rule name to make sure its unique (in a RuleSet).
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     * @param severity error severity.
     * @param errorMessage error message to be returned.
     * @param condition validating function.
     */
    public ValidationRule(String ruleName, String bindingName, String errorCode, Severity severity,
                          String errorMessage, Function<Binding<T>, Boolean> condition) {
        super(ruleName, bindingName, errorCode, severity, errorMessage);
        this.condition = condition;
    }

    @Override
    protected boolean when(Binding<T> binding) {
        return condition.apply(binding);
    }
}
