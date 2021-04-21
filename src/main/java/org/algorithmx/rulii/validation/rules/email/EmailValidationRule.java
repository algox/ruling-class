package org.algorithmx.rulii.validation.rules.email;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.validation.EmailValidator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value must match an email regex format.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must match an email regex pattern.")
public class EmailValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.EmailValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value not a valid email address. Given {0}.";

    private final boolean allowLocal;
    private final boolean allowTopLevelDomain;
    private final EmailValidator validator;

    public EmailValidationRule(String bindingName, boolean allowLocal, boolean allowTopLevelDomain) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, allowLocal, allowTopLevelDomain);
    }

    public EmailValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage,
                               boolean allowLocal, boolean allowTopLevelDomain) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.allowLocal = allowLocal;
        this.allowTopLevelDomain = allowTopLevelDomain;
        this.validator = EmailValidator.getInstance(allowLocal, allowTopLevelDomain);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("EmailValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return validator.isValid(value.toString());
    }

    public boolean isAllowLocal() {
        return allowLocal;
    }

    public boolean isAllowTopLevelDomain() {
        return allowTopLevelDomain;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "EmailValidationRule{" +
                "allowLocal=" + allowLocal +
                ", allowTopLevelDomain=" + allowTopLevelDomain +
                ", bindingName=" + getBindingName() +
                '}';
    }
}
