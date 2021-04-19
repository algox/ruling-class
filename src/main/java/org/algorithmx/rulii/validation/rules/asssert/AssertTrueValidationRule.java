package org.algorithmx.rulii.validation.rules.asssert;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the value is true.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be true.")
public class AssertTrueValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {boolean.class, Boolean.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.AssertTrueValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be true.";

    public AssertTrueValidationRule(String bindingName) {
        this(bindingName, bindingName);
    }

    public AssertTrueValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null);
    }

    public AssertTrueValidationRule(String bindingName, String path, String errorCode,
                                    Severity severity, String errorMessage) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof Boolean)) {
            throw new ValidationRuleException("AssertTrueValidationRule only applies to a boolean."
                    + "Supplied Class [" + value.getClass() + "]");
        }

        return Boolean.TRUE.equals(value);
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "AssertTrueValidationRule{"
                + "bindingName=" + getBindingName()
                + "}";
    }
}
