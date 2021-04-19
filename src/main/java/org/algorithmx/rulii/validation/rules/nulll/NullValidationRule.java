package org.algorithmx.rulii.validation.rules.nulll;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;

/**
 * Validation Rule to make sure the value is null.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be null.")
public class NullValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE      = "rulii.validation.rules.NullValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE = "{0} must be null. Given {1}.";

    public NullValidationRule(String bindingName) {
        this(bindingName, bindingName);
    }

    public NullValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null);
    }

    public NullValidationRule(String bindingName, String path, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return value == null;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "NullValidationRule{" + ", bindingName=" + getBindingName() + "}";
    }
}
