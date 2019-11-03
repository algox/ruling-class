package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.model.Severity;

import java.util.Date;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the Date binding is in the future.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Date binding is in the future.")
public class FutureDateRule extends BindingValidationRule<Date> {

    public FutureDateRule(String errorCode, String bindingName) {
        super(errorCode, Severity.FATAL, null, date -> isFuture(date), bindingName);
    }

    public FutureDateRule(String errorCode, Supplier<Binding<Date>> supplier) {
        super(errorCode, Severity.FATAL, null, date -> isFuture(date), supplier);
    }

    private static boolean isFuture(Date date) {
        Date currentDate = new Date();
        return date != null && currentDate.before(date);
    }

    @Override
    public String getErrorMessage() {
        if (super.getErrorMessage() != null) return super.getErrorMessage();
        String bindingName = getBindingName();
        if (bindingName == null) bindingName = "NOT BOUND";
        return "Date [" + bindingName + "] must be in the future. Given {" + bindingName + "}";
    }
}
