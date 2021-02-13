package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.core.rule.RuleDefinition;
import org.algorithmx.rulii.core.rule.RuleDefinitionAware;
import org.algorithmx.rulii.validation.RuleViolationBuilder;

public abstract class ValidationRule implements RuleDefinitionAware {

    private final String errorCode;
    private final Severity severity;
    private final String errorMessage;
    private final String defaultMessage;
    private RuleDefinition ruleDefinition;

    protected ValidationRule(String errorCode, Severity severity, String defaultMessage) {
        this(errorCode, severity, null, defaultMessage);
    }

    protected ValidationRule(String errorCode, Severity severity, String errorMessage, String defaultMessage) {
        super();
        this.errorCode = errorCode;
        this.severity = severity;
        this.errorMessage = errorMessage;
        this.defaultMessage = defaultMessage;
    }

    public String getName() {
        return ruleDefinition != null ? ruleDefinition.getName() : getClass().getSimpleName();
    }

    public String getErrorCode() {
        return errorCode;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public void setRuleDefinition(RuleDefinition ruleDefinition) {
        if (this.ruleDefinition != null) throw new UnrulyException("ruleDefinition cannot be modified.");
        this.ruleDefinition = ruleDefinition;
    }

    protected RuleViolationBuilder createRuleViolationBuilder() {
        return RuleViolationBuilder.with(this);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "errorCode='" + errorCode + '\'' +
                ", severity=" + severity +
                ", errorMessage='" + errorMessage + '\'' +
                ", defaultMessage='" + defaultMessage + '\'' +
                ", ruleDefinition=" + ruleDefinition +
                '}';
    }
}
