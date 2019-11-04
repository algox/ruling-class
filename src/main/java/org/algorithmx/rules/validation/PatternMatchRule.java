package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Validation Rule to make sure the String value matches the given regex pattern.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String value matches the given regex pattern.")
public class PatternMatchRule extends BindingValidationRule<String> {

    private final String pattern;

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param pattern desired pattern.
     * @param errorCode error code.
     * @param bindingName name of the Binding.
     */
    public PatternMatchRule(String pattern, String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, value -> matches(value, Pattern.compile(pattern)), bindingName);
        this.pattern = pattern;
    }

    /**
     * Ctor taking the error code and name of the Binding.
     *
     * @param pattern desired regex pattern.
     * @param errorCode error code.
     * @param supplier Binding.
     */
    public PatternMatchRule(String pattern, String errorCode, Supplier<Binding<String>> supplier) {
        super(errorCode, Severity.FATAL, null, value -> matches(value, Pattern.compile(pattern)), supplier);
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }

    /**
     * Determines whether the given value matches the desired regex pattern.
     *
     * @param value text value.
     * @param pattern desired pattern.
     * @return true if the text matches the pattern; false otherwise.
     */
    private static boolean matches(String value, Pattern pattern) {
        return value != null && pattern.matcher(value).matches();
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Regex Pattern not matched by [" + bindingName + "], it must be match [" + pattern + "]. Given {" + bindingName + "}";
    }
}
