package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.annotation.Given;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
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
        Object result = getBindingValue(context);

        if (result != null && !isSupported(result.getClass())) {
            // TODO : Log warning
            return false;
        }

        return isValid(context, result);
    }

    @Otherwise
    public void otherwise(RuleContext context, @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {
        Object value = getBindingValue(context);
        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("bindingName", getBindingName())
                .param(getBindingName(), value);
        customizeViolation(context, builder);
        errors.add(builder.build(context));
    }

    protected abstract boolean isValid(RuleContext context, Object value);

    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {}

    public String getBindingName() {
        return bindingName;
    }

    public Object getBindingValue(RuleContext context) {
        return context.getBindings().contains(getBindingName()) ? context.getBindings().getValue(getBindingName()) : null;
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
