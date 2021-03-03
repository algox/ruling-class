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
package org.algorithmx.rulii.validation.rules.min;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.util.NumberComparator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is greater than the desired Min.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is greater than the desired Min.")
public class MinValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.MinValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be greater than or equal to {2}. Given {1}.";

    private final long min;

    public MinValidationRule(String bindingName, long min) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, min);
    }

    public MinValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage, long min) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.min = min;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        Number number = null;

        if (value instanceof CharSequence) {
            try {
                number = new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                return false;
            }
        }
        if (value instanceof Number) number = (Number) value;

        if (number == null)
            throw new ValidationRuleException("MinValidationRule only applies to Numbers/CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        Integer result = NumberComparator.compare(number, min);
        return result == null ? true : result >= 0;
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder.param("min", min);
    }

    public long getMin() {
        return min;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "MinValidationRule{" +
                "min=" + min +
                ", bindingName=" + getBindingName() +
                '}';
    }
}
