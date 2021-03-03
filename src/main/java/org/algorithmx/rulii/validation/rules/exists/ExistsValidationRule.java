package org.algorithmx.rulii.validation.rules.exists;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.io.File;

/**
 * Validation Rule to make sure the file exists.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("File must exist.")
public class ExistsValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {File.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.ExistsValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} file must exist.";

    public ExistsValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public ExistsValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof File)) {
            throw new ValidationRuleException("ExistsValidationRule only applies to a File."
                    + "Supplied Class [" + value.getClass() + "]");
        }

        return ((File) value).exists();
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "ExistsValidationRule{"
                + "bindingName=" + getBindingName()
                + "}";
    }
}
