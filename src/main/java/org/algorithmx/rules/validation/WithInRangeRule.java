package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.Severity;

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
@Description("Value is in between the desired (min,max) range (inclusive).")
public class WithInRangeRule extends BindingValidationRule<Object> {

    private final long min;
    private final long max;

    public WithInRangeRule(long min, long max, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> isInRange(value, min, max), bindingName);
        this.min = min;
        this.max = max;
    }

    public WithInRangeRule(long min, long max, String errorCode, Supplier<Binding<Object>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> isInRange(value, min, max), supplier);
        this.min = min;
        this.max = max;
    }

    public long getMin() {
        return min;
    }

    public long getMax() {
        return max;
    }

    private static boolean isInRange(Object value, long min, long max) {
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

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Value for [" + bindingName + "] is out of range, it must be greater than [" + min
                + "] and less than [" + max + "]. Given {" + bindingName + "}";
    }
}
