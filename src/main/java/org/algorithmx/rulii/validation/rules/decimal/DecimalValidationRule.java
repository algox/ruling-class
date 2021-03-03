package org.algorithmx.rulii.validation.rules.decimal;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.math.BigDecimal;

/**
 * Validation Rule to make sure the the value is a decimal.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be a valid decimal.")
public class DecimalValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.DecimalValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be a valid decimal. Given {1}.";

    private final boolean allowSpace;

    public DecimalValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, true);
    }

    public DecimalValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage, boolean allowSpace) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.allowSpace = allowSpace;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("DecimalValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        String stringValue = isAllowSpace() ? value.toString().trim() : value.toString();

        try {
            new BigDecimal(stringValue);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public boolean isAllowSpace() {
        return allowSpace;
    }

    @Override
    public String toString() {
        return "DecimalValidationRule{"
                + "bindingName=" + getBindingName()
                + "}";
    }
}
