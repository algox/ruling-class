package org.algorithmx.rulii.validation.rules.endswith;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.util.Arrays;

/**
 * Validation Rule to make sure the the value must end with one of the given suffixes.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must end with one of the given suffixes.")
public class EndsWithValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.EndsWithValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must end with one of the given suffixes {2}. Given {1}.";

    private String[] suffixes;

    public EndsWithValidationRule(String bindingName, String...suffixes) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, suffixes);
    }

    public EndsWithValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage, String...suffixes) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(suffixes, "suffixes cannot be null.");
        this.suffixes = suffixes;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("EndsWithValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        boolean result = false;
        String stringValue = value.toString();

        for (String suffix : suffixes) {
            if (stringValue.endsWith(suffix)) {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder.param("suffixes", Arrays.toString(suffixes));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public String[] getSuffixes() {
        return suffixes;
    }

    @Override
    public String toString() {
        return "EndsWithValidationRule{"
                + "bindingName=" + getBindingName()
                + "suffixes=" + Arrays.toString(suffixes)
                + "}";
    }
}
