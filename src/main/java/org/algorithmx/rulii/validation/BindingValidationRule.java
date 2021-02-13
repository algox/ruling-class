package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.annotation.Given;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.StringUtils;
import org.algorithmx.rulii.lib.spring.util.Assert;

public abstract class BindingValidationRule extends ValidationRule {

    private final String bindingName;

    public BindingValidationRule(String bindingName, String errorCode, Severity severity, String defaultMessage) {
        super(errorCode, severity, defaultMessage);
        Assert.isTrue(StringUtils.isNotEmpty(bindingName), "bindingName must have text.");
        this.bindingName = bindingName;
    }

    public BindingValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage, String defaultMessage) {
        super(errorCode, severity, errorMessage, defaultMessage);
        Assert.isTrue(StringUtils.isNotEmpty(bindingName), "bindingName must have text.");
        this.bindingName = bindingName;
    }

    @Given
    public boolean isValid(RuleContext context) {
        Object result = context.getBindings().contains(bindingName) ? context.getBindings().getValue(bindingName) : null;
        return isValid(context, result);
    }

    protected abstract boolean isValid(RuleContext context, Object value);

    public String getBindingName() {
        return bindingName;
    }

    public abstract Class<?>[] getSupportedTypes();

    public boolean isSupported(Class<?> type) {
        boolean result = false;

        for (Class<?> c : getSupportedTypes()) {
            if (c.isAssignableFrom(type)) {
                result = true;
                break;
            }
        }

        return result;
    }
}
