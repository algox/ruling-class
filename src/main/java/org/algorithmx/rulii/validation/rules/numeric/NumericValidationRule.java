package org.algorithmx.rulii.validation.rules.numeric;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value is numeric.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be numeric.")
public class NumericValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NumericValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be numeric. Given {1}.";

    private final boolean allowSpace;

    public NumericValidationRule(String bindingName) {
        this(bindingName, bindingName);
    }

    public NumericValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null, false);
    }

    public NumericValidationRule(String bindingName, String path, String errorCode, Severity severity,
                                 String errorMessage, boolean allowSpace) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.allowSpace = allowSpace;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("UpperCaseValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return isAllowSpace() ? StringUtils.isNumericSpace((CharSequence) value) : StringUtils.isNumeric((CharSequence) value);
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
        return "NumericValidationRule{"
                + "bindingName=" + getBindingName()
                + "allowSpace=" + isAllowSpace()
                + "}";
    }
}
