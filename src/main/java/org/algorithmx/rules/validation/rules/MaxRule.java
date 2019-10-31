package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.FunctionalValidationRule;
import org.algorithmx.rules.validation.ValidationError;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the the value is less the desired Max.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description(" String binding matches the given regex pattern.")
public class MaxRule extends FunctionalValidationRule<Object> {

    private final long max;

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param max desired max value.
     * @param errorCode error code to be returned.
     */
    public MaxRule(Supplier<Binding<Object>> supplier, long max, String errorCode) {
        this(supplier, max, errorCode, Severity.FATAL, "["
                + getBindingName(supplier) + "] must be null.");
    }

    /**
     * Ctor taking in the binding supplier, error code, severity and error message.
     *
     * @param supplier binding supplier.
     * @param max desired max value.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public MaxRule(Supplier<Binding<Object>> supplier, long max, String errorCode, Severity severity,
                   String errorMessage) {
        this(supplier, max, new ValidationError(getBindingName(supplier) + "_" + MaxRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param max desired max value.
     * @param error validation error.
     */
    public MaxRule(Supplier<Binding<Object>> supplier, long max, ValidationError error) {
        super(supplier, binding -> match(binding, max), error);
        this.max = max;
    }

    public long getMax() {
        return max;
    }

    private static boolean match(Binding<Object> binding, long max) {
        if (binding == null) return false;

        Object value = binding.getValue();

        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.longValue() <= max;
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() <= max;
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() <= max;
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() <= max;
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) <= max;
        }

        throw new UnrulyException("MaxRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, string, collections, maps and arrays");
    }
}
