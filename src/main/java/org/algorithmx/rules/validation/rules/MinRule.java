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
public class MinRule extends FunctionalValidationRule<Object> {

    private final long min;

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param min desired min value.
     * @param errorCode error code to be returned.
     */
    public MinRule(Supplier<Binding<Object>> supplier, long min, String errorCode) {
        this(supplier, min, errorCode, Severity.FATAL, "["
                + getBindingName(supplier) + "] must be null.");
    }

    /**
     * Ctor taking in the binding supplier, error code, severity and error message.
     *
     * @param supplier binding supplier.
     * @param min desired min value.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public MinRule(Supplier<Binding<Object>> supplier, long min, String errorCode, Severity severity,
                   String errorMessage) {
        this(supplier, min, new ValidationError(getBindingName(supplier) + "_" + MinRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param min desired min value.
     * @param error validation error.
     */
    public MinRule(Supplier<Binding<Object>> supplier, long min, ValidationError error) {
        super(supplier, binding -> match(binding, min), error);
        this.min = min;
    }

    public long getMin() {
        return min;
    }

    private static boolean match(Binding<Object> binding, long min) {
        if (binding == null) return false;

        Object value = binding.getValue();

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
}
