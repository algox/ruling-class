package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.FunctionalValidationRule;
import org.algorithmx.rules.validation.ValidationError;

import java.util.function.Supplier;

/**
 * Validation Rule to make sure the String binding has text.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String binding has text.")
public class StringHasTextRule extends FunctionalValidationRule<String> {

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param errorCode error code to be returned.
     */
    public StringHasTextRule(Supplier<Binding<String>> supplier, String errorCode) {
        this(supplier, errorCode, Severity.FATAL, "["
                + getBindingName(supplier) + "] must be null.");
    }

    /**
     * Ctor taking in the binding supplier, error code, severity and error message.
     *
     * @param supplier binding supplier.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public StringHasTextRule(Supplier<Binding<String>> supplier, String errorCode, Severity severity, String errorMessage) {
        this(supplier, new ValidationError(getBindingName(supplier) + "_" + NullRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param error validation error.
     */
    public StringHasTextRule(Supplier<Binding<String>> supplier, ValidationError error) {
        super(supplier, binding -> binding != null && hasText(binding.get()), error);
    }

    private static boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();

        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }

        return false;
    }

}
