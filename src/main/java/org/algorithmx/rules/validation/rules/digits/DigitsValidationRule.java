package org.algorithmx.rules.validation.rules.digits;

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
import org.algorithmx.rules.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value must be within in range of maximum number of integral digits and
 * maximum number of fractional digits.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be within range of the maximum integral digits and maximum fraction digits.")
public class DigitsValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.DigitsValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must have at most {1} integral digits and {2} fraction digits. " +
            "Given {0}.";

    private final int maxIntegerLength;
    private final int maxFractionLength;

    public DigitsValidationRule(int maxIntegerLength, int maxFractionLength) {
        this(ERROR_CODE, Severity.ERROR, null, maxIntegerLength, maxFractionLength);
    }

    public DigitsValidationRule(String errorCode, Severity severity, String errorMessage,
                                int maxIntegerLength, int maxFractionLength) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.maxIntegerLength = maxIntegerLength;
        this.maxFractionLength = maxFractionLength;
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        BigDecimal number = null;

        if (value instanceof BigDecimal ) {
            number = (BigDecimal) value;
        } else if (value instanceof Number || value instanceof CharSequence){
            try {
                number = new BigDecimal(value.toString());
                number = number.stripTrailingZeros();
            } catch (NumberFormatException e) {
                return false;
            }
        }

        if (number == null) {
            throw new ValidationRuleException("DigitsValidationRule only applies to Numbers/CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");
        }


        int integerLength = number.precision() - number.scale();
        int fractionLength = number.scale() < 0 ? 0 : number.scale();

        return (maxIntegerLength >= integerLength && maxFractionLength >= fractionLength);
    }

    @Otherwise
    public void otherwise(Object value, RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value)
                .param("maxIntegerLength", maxIntegerLength)
                .param("maxFractionLength", maxFractionLength);

        errors.add(builder.build(context));
    }

    public int getMaxIntegerLength() {
        return maxIntegerLength;
    }

    public int getMaxFractionLength() {
        return maxFractionLength;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "DigitsValidationRule{" +
                "maxIntegerLength=" + maxIntegerLength +
                ", maxFractionLength=" + maxFractionLength +
                '}';
    }
}
