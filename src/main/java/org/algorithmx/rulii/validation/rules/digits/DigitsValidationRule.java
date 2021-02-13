package org.algorithmx.rulii.validation.rules.digits;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

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
public class DigitsValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.DigitsValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must have at most {2} integral digits and {3} fraction digits. Given {1}.";

    private final int maxIntegerLength;
    private final int maxFractionLength;

    public DigitsValidationRule(String bindingName, int maxIntegerLength, int maxFractionLength) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, maxIntegerLength, maxFractionLength);
    }

    public DigitsValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage,
                                int maxIntegerLength, int maxFractionLength) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.maxIntegerLength = maxIntegerLength;
        this.maxFractionLength = maxFractionLength;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
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
                .param("bindingName", getBindingName())
                .param(getBindingName(), value)
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
                ", bindingName=" + getBindingName() +
                '}';
    }
}
