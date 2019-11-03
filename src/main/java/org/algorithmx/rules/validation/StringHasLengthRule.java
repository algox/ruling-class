package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;

import java.util.function.Supplier;

/**
 * Validation Rule to make sure the String value has length.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value has length.")
public class StringHasLengthRule extends BindingValidationRule<String> {

    public StringHasLengthRule(String pattern, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> hasLength(value), bindingName);
    }

    public StringHasLengthRule(String pattern, String errorCode, Supplier<Binding<String>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> hasLength(value), supplier);
    }

    private static boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Binding [" + bindingName + "] does not have any length. Given {" + bindingName + "}";
    }
}
