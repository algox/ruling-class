package org.algorithmx.rulii.validation.rules.negative;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.util.NumberComparator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is less than 0.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than 0.")
public class NegativeValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NegativeValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be less than 0. Given {0}.";

    public NegativeValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public NegativeValidationRule(String bindingName, String errorCode,
                                  Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
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
            throw new ValidationRuleException("NegativeValidationRule only applies to Numbers/CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        Integer result = NumberComparator.signum(number);
        return result == null ? true : result < 0;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "NegativeValidationRule{" + ", bindingName=" + getBindingName() + "}";
    }
}
