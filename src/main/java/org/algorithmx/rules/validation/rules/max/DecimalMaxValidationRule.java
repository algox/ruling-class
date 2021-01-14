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
package org.algorithmx.rules.validation.rules.max;

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
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.util.NumberComparator;
import org.algorithmx.rules.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is less the desired Max.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than the desired Max.")
public class DecimalMaxValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.DecimalMaxValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be less than or equal to {1}. Given {0}.";

    private final BigDecimal max;
    private final boolean inclusive;

    public DecimalMaxValidationRule(BigDecimal max, boolean inclusive) {
        this(ERROR_CODE, Severity.ERROR, null, max, inclusive);
    }

    public DecimalMaxValidationRule(String errorCode, Severity severity, String errorMessage, BigDecimal max, boolean inclusive) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(max, "max cannot be null.");
        this.max = max;
        this.inclusive = inclusive;
    }

    @Given
    public boolean isValid(Object value) {
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

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value)
                .param("max", max);

        errors.add(builder.build(context));
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
                '}';
    }
}
