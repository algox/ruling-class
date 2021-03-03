package org.algorithmx.rulii.validation.rules.startswith;

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
 * Validation Rule to make sure the the value must start with one of the given prefixes.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must start with one of the given prefixes.")
public class StartsWithValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.StartsWithValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must start with one of the given prefixes {2}. Given {1}.";

    private String[] prefixes;

    public StartsWithValidationRule(String bindingName, String...prefixes) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, prefixes);
    }

    public StartsWithValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage, String...prefixes) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(prefixes, "prefixes cannot be null.");
        this.prefixes = prefixes;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("StartsWithValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        boolean result = false;
        String stringValue = value.toString();

        for (String prefix : prefixes) {
            if (stringValue.startsWith(prefix)) {
                result = true;
                break;
            }
        }

        return result;
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder.param("prefixes", Arrays.toString(prefixes));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public String[] getPrefixes() {
        return prefixes;
    }

    @Override
    public String toString() {
        return "EndsWithValidationRule{"
                + "bindingName=" + getBindingName()
                + "prefixes=" + Arrays.toString(prefixes)
                + "}";
    }
}
