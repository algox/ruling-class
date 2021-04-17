package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.annotation.Given;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.PreCondition;
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

    @PreCondition
    public boolean checkType(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context) {
        Object value = getBindingValue(context);
        boolean result = value == null || isSupported(value.getClass());
        if (!result) {
            //System.err.println("XXX Rule [" + getClass().getSimpleName()
            //        + "] Value [" + value.getClass() + "] [" + Arrays.toString(getSupportedTypes()) + "]");
        }// TODO : Log warning
        return result;
    }

    @Given
    public boolean isValid(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context) {
        return isValid(context, getBindingValue(context));
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations violations) {
        Object value = getBindingValue(context);
        RuleViolationBuilder builder = createRuleViolationBuilder()
                //.param("bindingName", getBindingName())
                .param("value", value);
        customizeViolation(context, builder);
        violations.add(builder.build(context));
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
