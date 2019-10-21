package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.ValidationRuleTemplate;

/**
 * Validation Rule to make sure the String binding has length.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String binding has length.")
public class StringHasLengthRule extends ValidationRuleTemplate<String> {

    /**
     * Ctor taking in the binding name and error code.
     *
     * @param bindingName name of the Binding.
     * @param errorCode error code to be returned.
     */
    public StringHasLengthRule(String bindingName, String errorCode) {
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
    public StringHasLengthRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName + "_" + StringHasLengthRule.class.getSimpleName(), bindingName, errorCode, severity, errorMessage);
    }

    @Override
    protected boolean when(Binding<String> binding) {
        return binding != null && hasLength(binding.get());
    }

    private boolean hasLength(String str) {
        return (str != null && !str.isEmpty());
    }
}
