package org.algorithmx.rulii.validation.rules.alpha;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value only contains unicode letters (or spaces).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value can only contain unicode letters/spaces.")
public class AlphaValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.AlphaValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must only contain unicode letters. Given {1}.";

    private final boolean allowSpace;

    public AlphaValidationRule(String bindingName) {
        this(bindingName, bindingName);
    }

    public AlphaValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null, true);
    }

    public AlphaValidationRule(String bindingName, String path, String errorCode, Severity severity,
                               String errorMessage, boolean allowSpace) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.allowSpace = allowSpace;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("AlphaValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return isAllowSpace() ? StringUtils.isAlphaSpace((CharSequence) value) : StringUtils.isAlpha((CharSequence) value);
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
        return "AlphaValidationRule{"
                + "bindingName=" + getBindingName()
                + "allowSpace=" + isAllowSpace()
                + "}";
    }
}
