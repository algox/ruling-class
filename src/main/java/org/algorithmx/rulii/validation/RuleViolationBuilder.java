package org.algorithmx.rulii.validation;

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.text.MessageFormatter;
import org.algorithmx.rulii.text.MessageResolver;
import org.algorithmx.rulii.text.ParameterInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RuleViolationBuilder {

    private final String ruleName;
    private String errorCode = null;
    private Severity severity = null;
    private String errorMessage = null;
    private String defaultMessage = null;
    private final List<ParameterInfo> params = new ArrayList<>();

    private RuleViolationBuilder(String ruleName) {
        super();
        Assert.notNull(ruleName, "ruleName cannot be null.");
        this.ruleName = ruleName;
    }

    public static RuleViolationBuilder with(String ruleName) {
        return new RuleViolationBuilder(ruleName);
    }

    public static RuleViolationBuilder with(ValidationRule rule) {
        Assert.notNull(rule, "rule cannot be null.");
        RuleViolationBuilder result = new RuleViolationBuilder(rule.getName())
                .errorCode(rule.getErrorCode())
                .severity(rule.getSeverity())
                .errorMessage(rule.getErrorMessage())
                .defaultMessage(rule.getDefaultMessage());
        return result;
    }

    public RuleViolationBuilder errorCode(String errorCode) {
        this.errorCode = errorCode;
        return this;
    }

    public RuleViolationBuilder errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public RuleViolationBuilder defaultMessage(String defaultMessage) {
        this.defaultMessage = defaultMessage;
        return this;
    }

    public RuleViolationBuilder severity(Severity severity) {
        this.severity = severity;
        return this;
    }

    public RuleViolationBuilder param(String name, Object value) {
        params.add(new ParameterInfo(params.size(), name, value));
        return this;
    }

    public RuleViolationBuilder clearParams() {
        this.params.clear();
        return this;
    }

    public RuleViolation build() {
        return build(null, null , null);
    }

    public RuleViolation build(MessageResolver messageResolver, MessageFormatter messageFormatter, Locale locale) {
        String message = messageResolver != null ? resolveErrorMessage(messageResolver, locale) : null;
        RuleViolation result = new RuleViolation(ruleName, errorCode, severity,
                message != null
                        ? messageFormatter.format(locale, message, params.toArray(new ParameterInfo[params.size()]))
                        : null);

        addRuleParameters(result, params);
        return result;
    }

    private String resolveErrorMessage(MessageResolver messageResolver, Locale locale) {
        if (errorMessage != null) return errorMessage;
        return messageResolver != null ? messageResolver.resolve(locale, errorCode, defaultMessage) : null;
    }

    private void addRuleParameters(RuleViolation error, List<ParameterInfo> parameters) {
        parameters.stream()
                .filter(m -> m != null)
                .forEach(m -> error.param(m.getName(),
                        m.getValue() != null
                                ? m.getValue().toString()
                                : null));
    }
}
