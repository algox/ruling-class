package org.algorithmx.rulii.validation.rules.notempty;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Validation Rule to make sure the value is not empty (ie. arrays/collections/maps/strings have at least one item).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must not be empty.")
public class NotEmptyValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {boolean[].class, byte[].class, char[].class, double[].class, float[].class,
            int[].class, long[].class, short[].class, Object[].class, Collection.class, Map.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NotEmptyValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} cannot be empty.";

    public NotEmptyValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public NotEmptyValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (value instanceof Collection) return isValid((Collection) value);
        if (value instanceof CharSequence) return isValid((CharSequence) value);
        if (value instanceof Map) return isValid((Map) value);
        if (value.getClass().isArray()) return isValidArray(value);

        throw new ValidationRuleException("NotEmptyValidationRule only applies to Collections/Maps/Arrays and CharSequences."
                + "Supplied Class [" + value.getClass() + "]");
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    private boolean isValidArray(Object value) {
        int length = Array.getLength(value);
        return length > 0;
    }

    private boolean isValid(Collection collection) {
        int length = collection.size();
        return length > 0;
    }

    private boolean isValid(Map map) {
        int length = map.size();
        return length > 0;
    }

    private boolean isValid(CharSequence sequence) {
        int length = sequence.length();
        return length > 0;
    }

    @Override
    public String toString() {
        return "NotEmptyValidationRule{" + ", bindingName=" + getBindingName() + "}";
    }
}
