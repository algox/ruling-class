package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;

import java.util.function.Supplier;

/**
 * Validation Rule to make sure the String value has text in it.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value has text in it.")
public class StringHasTextRule extends BindingValidationRule<String> {

    public StringHasTextRule(String pattern, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> hasText(value), bindingName);
    }

    public StringHasTextRule(String pattern, String errorCode, Supplier<Binding<String>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> hasText(value), supplier);
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

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Binding [" + bindingName + "] does not have any length. Given {" + bindingName + "}";
    }

}
