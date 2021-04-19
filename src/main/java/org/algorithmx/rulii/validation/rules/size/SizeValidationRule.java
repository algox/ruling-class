package org.algorithmx.rulii.validation.rules.size;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Validation Rule to make sure the size is between the given min and max values.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Size is between the given min and max values.")
public class SizeValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {boolean[].class, byte[].class, char[].class, double[].class, float[].class,
            int[].class, long[].class, short[].class, Object[].class, Collection.class, Map.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.SizeValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be between {2} and {3}. Given {1}.";

    private final int min;
    private final int max;

    public SizeValidationRule(String bindingName, int min, int max) {
        this(bindingName, bindingName, min, max);
    }

    public SizeValidationRule(String bindingName, String path, int min, int max) {
        this(bindingName, path, ERROR_CODE, Severity.ERROR, null, min, max);
    }

    public SizeValidationRule(String bindingName, String path, String errorCode, Severity severity,
                              String errorMessage, int min, int max) {
        super(bindingName, path, errorCode, severity, errorMessage);
        Assert.isTrue(min >= 0, "min >= 0");
        Assert.isTrue(max >= 0, "max >= 0");
        Assert.isTrue(max >= min, "max >= min");
        this.min = min;
        this.max = max;
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        Integer size = null;

        if (value instanceof CharSequence) size = getSize((CharSequence) value);
        if (value instanceof Collection) size = getSize((Collection) value);
        if (value instanceof Map) size = getSize((Map) value);
        if (value.getClass().isArray()) size = getSize(value);

        if (size == null) {
            throw new ValidationRuleException("SizeValidationRule only applies to Collections/Maps/CharSequences and Arrays."
                    + "Supplied Class [" + value.getClass() + "]");
        }

        return size >= min && size <= max;
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder
                .param("min", min)
                .param("max", max);
    }

    private int getSize(Object value) {
        return Array.getLength(value);
    }

    private int getSize(Collection collection) {
        return collection.size();
    }

    private int getSize(Map map) {
        return map.size();
    }

    private int getSize(CharSequence sequence) {
        return sequence.length();
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "SizeValidationRule{" +
                "min=" + min +
                ", max=" + max +
                ", bindingName=" + getBindingName() +
                '}';
    }
}
