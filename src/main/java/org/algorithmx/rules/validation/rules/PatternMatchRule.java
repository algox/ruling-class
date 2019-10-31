package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.FunctionalValidationRule;
import org.algorithmx.rules.validation.ValidationError;

import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Validation Rule to make sure the String binding matches the given regex pattern.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description(" String binding matches the given regex pattern.")
public class PatternMatchRule extends FunctionalValidationRule<String> {

    private final Pattern pattern;

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param pattern desired regex pattern.
     * @param errorCode error code to be returned.
     */
    public PatternMatchRule(Supplier<Binding<String>> supplier, String pattern, String errorCode) {
        this(supplier, pattern, errorCode, Severity.FATAL, "["
                + getBindingName(supplier) + "] must be null.");
    }

    /**
     * Ctor taking in the binding supplier, error code, severity and error message.
     *
     * @param supplier binding supplier.
     * @param pattern desired regex pattern.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public PatternMatchRule(Supplier<Binding<String>> supplier, String pattern, String errorCode, Severity severity,
                            String errorMessage) {
        this(supplier, pattern, new ValidationError(getBindingName(supplier) + "_" + PatternMatchRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param pattern desired regex pattern.
     * @param error validation error.
     */
    public PatternMatchRule(Supplier<Binding<String>> supplier, String pattern, ValidationError error) {
        super(supplier, binding -> match(binding, Pattern.compile(pattern)), error);
        this.pattern = Pattern.compile(pattern);
    }

    public Pattern getPattern() {
        return pattern;
    }

    private static boolean match(Binding<String> binding, Pattern pattern) {
        return binding != null && pattern.matcher(binding.get()).matches();
    }
}
