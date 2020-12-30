package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.rule.RuleDefinitionAware;

public abstract class ValidationRule implements RuleDefinitionAware {

    private String errorCode = null;
    private Severity severity = Severity.ERROR;
    private String errorMessage = null;
    private String defaultMessage= null;
    private RuleDefinition ruleDefinition;

    protected ValidationRule() {
        super();
    }

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

    public ValidationRule errorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public Severity getSeverity() {
        return severity;
    }

    public ValidationRule severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public ValidationRule errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }

    public ValidationRule defaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }

    @Override
    public void setRuleDefinition(RuleDefinition ruleDefinition) {
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
