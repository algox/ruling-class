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
 * Validation Rule to make sure the the value is less the desired Min.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is less than the desired Min.")
public class MinRule extends BindingValidationRule<Object> {

    private final long min;

    public MinRule(long min, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> isGreaterThanMin(value, min), bindingName);
        this.min = min;
    }

    public MinRule(long min, String errorCode, Supplier<Binding<Object>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> isGreaterThanMin(value, min), supplier);
        this.min = min;
    }


    public long getMin() {
        return min;
    }

    private static boolean isGreaterThanMin(Object value, long min) {
        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.longValue() >= min;
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() >= min;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() >= min;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() >= min;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) >= min;
        }

        throw new UnrulyException("MinRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, strings, collections, maps and arrays");
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Min value reached for [" + bindingName + "], it must be greater than [" + min + "]. Given {" + bindingName + "}";
    }
}
