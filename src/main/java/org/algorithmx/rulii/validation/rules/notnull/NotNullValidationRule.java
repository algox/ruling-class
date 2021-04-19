package org.algorithmx.rulii.validation.rules.notnull;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;

/**
 * Validation Rule to make sure the value is not null.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value cannot be null.")
public class NotNullValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NotNullValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} cannot be null.";

    public NotNullValidationRule(String bindingName) {
        this(bindingName, bindingName);
    }

    public NotNullValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null);
    }

    public NotNullValidationRule(String bindingName, String path, String errorCode,
                                 Severity severity, String errorMessage) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return value != null;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "NotNullValidationRule{bindingName=" + getBindingName() + "}";
    }
}
