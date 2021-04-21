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
package org.algorithmx.rulii.validation.rules.max;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.NumberComparator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is less the desired Max.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than the desired Max.")
public class DecimalMaxValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.DecimalMaxValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be less than or equal to {1}. Given {0}.";

    private final BigDecimal max;
    private final boolean inclusive;

    public DecimalMaxValidationRule(String bindingName, BigDecimal max, boolean inclusive) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, max, inclusive);
    }

    public DecimalMaxValidationRule(String bindingName, String errorCode, Severity severity,
                                    String errorMessage, BigDecimal max, boolean inclusive) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(max, "max cannot be null.");
        this.max = max;
        this.inclusive = inclusive;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        Number number = null;

        if (value instanceof Number) number = (Number) value;
        if (value instanceof CharSequence) {
            try {
                number = new BigDecimal(value.toString());
            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (number == null)
            throw new ValidationRuleException("DecimalMaxValidationRule only applies to Numbers/CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        Integer result = NumberComparator.compare(number, max);
        return result == null
                ? true
                : isInclusive()
                    ? result <= 0
                    : result < 0;
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder.param("max", max);
    }

    public BigDecimal getMax() {
        return max;
    }

    public boolean isInclusive() {
        return inclusive;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "DecimalMaxValidationRule{" +
                "max=" + max +
                ", inclusive=" + inclusive +
                ", bindingName=" + getBindingName() +
                '}';
    }
}
