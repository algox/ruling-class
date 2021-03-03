package org.algorithmx.rulii.validation.rules.blank;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the value is blank.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be blank.")
public class BlankValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.BlankValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be blank.";

    public BlankValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public BlankValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("BlankValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "]");

        // Make sure there some text
        return StringUtils.isBlank((CharSequence) value);
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "BlankValidationRule{" + ", bindingName=" + getBindingName() + "}";
    }
}
