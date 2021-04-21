package org.algorithmx.rulii.validation.rules.alphnumeric;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value only contains alphanumeric letters (or spaces).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value can only contain unicode alphanumeric letters/spaces.")
public class AlphaNumericValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.AlphaNumericValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must only contain alphanumeric letters. Given {0}.";

    private final boolean allowSpace;

    public AlphaNumericValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, true);
    }

    public AlphaNumericValidationRule(String bindingName, String errorCode, Severity severity,
                                      String errorMessage, boolean allowSpace) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.allowSpace = allowSpace;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("AlphaNumericValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return isAllowSpace() ? StringUtils.isAlphanumericSpace((CharSequence) value) : StringUtils.isAlphanumeric((CharSequence) value);
    }

    public boolean isAllowSpace() {
        return allowSpace;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "AlphaNumericValidationRule{"
                + "bindingName=" + getBindingName()
                + "isAllowSpace=" + isAllowSpace()
                + "}";
    }
}
