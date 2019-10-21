package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.ValidationRuleTemplate;

import java.util.regex.Pattern;

/**
 * Validation Rule to make sure the String binding matches the given regex pattern.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description(" String binding matches the given regex pattern.")
public class PatternMatchRule extends ValidationRuleTemplate<String> {

    private final Pattern pattern;

    /**
     * Ctor taking in the binding name and error code.
     *
     * @param bindingName name of the Binding.
     * @param pattern regex pattern.
     * @param errorCode error code to be returned.
     */
    public PatternMatchRule(String bindingName, String pattern, String errorCode) {
        this(bindingName, pattern, errorCode, Severity.FATAL, "[" + bindingName + "] must be length.");
    }

    /**
     * Ctor taking in the binding name, error code and error message.
     *
     * @param bindingName name of the Binding.
     * @param pattern regex pattern.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public PatternMatchRule(String bindingName, String pattern, String errorCode, Severity severity, String errorMessage) {
        super(bindingName + "_" + PatternMatchRule.class.getSimpleName(), bindingName, errorCode, severity, errorMessage);
        this.pattern = Pattern.compile(pattern);
    }

    @Override
    protected boolean when(Binding<String> binding) {
        return binding != null && pattern.matcher(binding.get()).matches();
    }
}
