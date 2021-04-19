package org.algorithmx.rulii.validation.rules.positive;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.util.NumberComparator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is greater than or equal 0.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is greater than or equal 0.")
public class PositiveOrZeroValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Number.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.PositiveOrZeroValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be greater than or equal to 0. Given {1}.";

    public PositiveOrZeroValidationRule(String bindingName) {
        this(bindingName, bindingName);
    }

    public PositiveOrZeroValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null);
    }

    public PositiveOrZeroValidationRule(String bindingName, String path, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
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
            throw new ValidationRuleException("PositiveOrZeroValidationRule only applies to Numbers/CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        Integer result = NumberComparator.signum(number);
        return result == null ? true : result >= 0;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "PositiveOrZeroValidationRule{" + "bindingName=" + getBindingName() + "}";
    }
}
