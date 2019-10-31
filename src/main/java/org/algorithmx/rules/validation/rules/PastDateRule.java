package org.algorithmx.rules.validation.rules;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.validation.FunctionalValidationRule;
import org.algorithmx.rules.validation.ValidationError;

import java.util.Date;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the Date binding is in the past.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("String binding has length.")
public class PastDateRule extends FunctionalValidationRule<Date> {

    /**
     * Ctor taking in the binding supplier and error code.
     *
     * @param supplier binding supplier.
     * @param errorCode error code to be returned.
     */
    public PastDateRule(Supplier<Binding<Date>> supplier, String errorCode) {
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
    public PastDateRule(Supplier<Binding<Date>> supplier, String errorCode, Severity severity, String errorMessage) {
        this(supplier, new ValidationError(getBindingName(supplier) + "_" + PastDateRule.class.getSimpleName(),
                errorCode, severity, errorMessage));
    }

    /**
     * Ctor taking in the binding supplier and error.
     *
     * @param supplier binding supplier.
     * @param error validation error.
     */
    public PastDateRule(Supplier<Binding<Date>> supplier, ValidationError error) {
        super(supplier, binding -> binding != null && isPast(binding.get()), error);
    }

    private static boolean isPast(Date date) {
        Date currentDate = new Date();
        return date != null && currentDate.after(date);
    }
}
