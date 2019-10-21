package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.ValidationRuleTemplate;

/**
 * Validation Rule to make sure the String binding has text.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String binding has text.")
public class StringHasTextRule extends ValidationRuleTemplate<String> {

    /**
     * Ctor taking in the binding name and error code.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     */
    public StringHasTextRule(String bindingName, String errorCode) {
        this(bindingName, errorCode, Severity.FATAL, "[" + bindingName + "] must be length.");
    }

    /**
     * Ctor taking in the binding name, error code and error message.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     * @param severity severity of the error.
     * @param errorMessage error message to be returned.
     */
    public StringHasTextRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName + "_" + StringHasTextRule.class.getSimpleName(), bindingName, errorCode, severity, errorMessage);
    }

    @Override
    protected boolean when(Binding<String> binding) {
        return binding != null && hasText(binding.get());
    }

    private boolean hasText(String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

}
