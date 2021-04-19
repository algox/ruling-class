package org.algorithmx.rulii.validation.rules.lowercase;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.util.Set;

/**
 * Validation Rule to make sure the the value must be all in lowercase.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be all in lowercase.")
public class LowerCaseValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.LowerCaseValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be all in lowercase. Given {1}.";

    public LowerCaseValidationRule(String bindingName, Set<?> values) {
        this(bindingName, bindingName);
    }

    public LowerCaseValidationRule(String bindingName, String path) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null);
    }

    public LowerCaseValidationRule(String bindingName, String path, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("LowerCaseValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return StringUtils.isAllLowerCase((CharSequence) value);
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "LowerCaseValidationRule{"
                + "bindingName=" + getBindingName()
                + "}";
    }
}
