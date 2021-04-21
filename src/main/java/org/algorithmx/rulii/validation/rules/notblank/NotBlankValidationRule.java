package org.algorithmx.rulii.validation.rules.notblank;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the value is not blank (ie. it has some text).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value cannot be blank.")
public class NotBlankValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NotBlankValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value cannot be blank.";

    public NotBlankValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public NotBlankValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("NotBlankValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "]");

        // Make sure there some text
        return StringUtils.isNotBlank((CharSequence) value);
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "NotBlankValidationRule{" + ", bindingName=" + getBindingName() + "}";
    }
}
