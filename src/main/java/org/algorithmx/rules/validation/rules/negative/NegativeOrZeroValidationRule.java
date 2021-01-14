package org.algorithmx.rules.validation.rules.negative;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.util.NumberComparator;
import org.algorithmx.rules.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is less than or equal 0.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than or equal 0.")
public class NegativeOrZeroValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NegativeOrZeroValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be less than or equal to 0. Given {0}.";

    public NegativeOrZeroValidationRule() {
        this(ERROR_CODE, Severity.ERROR, null);
    }

    public NegativeOrZeroValidationRule(String errorCode, Severity severity, String errorMessage) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
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
            throw new ValidationRuleException("NegativeOrZeroValidationRule only applies to Numbers/CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        Integer result = NumberComparator.signum(number);
        return result == null ? true : result <= 0;
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value);

        errors.add(builder.build(context));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "NegativeOrZeroValidationRule{}";
    }
}
