package org.algorithmx.rulii.validation.rules.in;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.Severity;

import java.util.Set;

/**
 * Validation Rule to make sure the the value is in the given set.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be in the given set.")
public class InValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.InValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be in one of the given values {2}. Given {1}.";

    private final Set<?> values;

    public InValidationRule(String bindingName, Set<?> values) {
        this(bindingName, bindingName, values);
    }

    public InValidationRule(String bindingName, String path, Set<?> values) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null, values);
    }

    public InValidationRule(String bindingName, String path, String errorCode, Severity severity, String errorMessage,
                            Set<?> values) {
        super(bindingName, path, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(values, "values cannot be null.");
        this.values = values;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return values.contains(value);
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder.param("values", values.toString());
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public Set<?> getValues() {
        return values;
    }

    @Override
    public String toString() {
        return "EndsWithValidationRule{"
                + "bindingName=" + getBindingName()
                + "values=" + values.toString()
                + "}";
    }
}
