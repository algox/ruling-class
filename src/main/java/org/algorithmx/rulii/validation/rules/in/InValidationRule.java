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
        this(bindingName, ERROR_CODE, Severity.ERROR, null, values);
    }

    public InValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage,
                            Set<?> values) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(values, "values cannot be null.");
        this.values = values;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return values.contains(value);
    }

    /*protected Set<Object> convertValues(String[] values, Class<?> type, ConverterRegistry registry) {
        Set<Object> result = new HashSet<>();

        Converter converter = registry.find(String.class, type);

        if (converter == null)
            throw new ValidationRuleException("Could not convert [" + Arrays.toString(values) + "] to an array of ["
                    + type + "]. ConverterRegistry does not have a converter that can convert from String to ["
                    + type + "]. Register a converter and try again.");

        for (String value : values) {
            result.add(converter.convert(value, type));
        }

        return result;
    }*/

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
