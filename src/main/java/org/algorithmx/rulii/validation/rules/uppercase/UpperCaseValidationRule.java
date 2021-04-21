package org.algorithmx.rulii.validation.rules.uppercase;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value must be all in uppercase.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be all in uppercase.")
public class UpperCaseValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.UpperCaseValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be all in uppercase. Given {0}.";

    public UpperCaseValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public UpperCaseValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("UpperCaseValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return StringUtils.isAllUpperCase((CharSequence) value);
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "UpperCaseValidationRule{"
                + "bindingName=" + getBindingName()
                + "}";
    }
}
