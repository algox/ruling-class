package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.validation.FunctionalValidationRule;
import org.algorithmx.rules.validation.ValidationError;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the the value is in between the desired (min,max) range (inclusive).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description(" String binding matches the given regex pattern.")
public class WithInRangeRule extends FunctionalValidationRule<Object> {

    private final long min;
    private final long max;

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param min desired min value.
     * @param max desired max value.
     * @param errorCode error code to be returned.
     */
    public WithInRangeRule(Supplier<Binding<Object>> supplier, long min, long max, String errorCode) {
        this(supplier, min, max, errorCode, Severity.FATAL, "["
                + getBindingName(supplier) + "] must be null.");
    }

    /**
     * Ctor taking in the binding supplier, error code, severity and error message.
     *
     * @param supplier binding supplier.
     * @param min desired min value.
     * @param max desired max value.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public WithInRangeRule(Supplier<Binding<Object>> supplier, long min, long max, String errorCode, Severity severity,
                           String errorMessage) {
        this(supplier, min, max, new ValidationError(getBindingName(supplier) + "_" + WithInRangeRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param min desired min value.
     * @param max desired max value.
     * @param error validation error.
     */
    public WithInRangeRule(Supplier<Binding<Object>> supplier, long min, long max, ValidationError error) {
        super(supplier, binding -> match(binding, min, max), error);
        Assert.isTrue(min <= max, "min must be <= max. min [" + min + "] max [" + max + "]");
        this.min = min;
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    private static boolean match(Binding<Object> binding, long min, long max) {
        if (binding == null) return false;

        Object value = binding.getValue();

        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.longValue() >= min && number.longValue() <= max;
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() >= min && text.length() <= max;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() >= min && collection.size() <= max;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() >= min && map.size() <= max;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) >= min && Array.getLength(value) <= max;
        }

        throw new UnrulyException("WithInRangeRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, string, collections, maps and arrays");
    }
}
