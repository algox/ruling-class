package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Bind;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategyType;
import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.core.RuleContext;
import org.algorithmx.rules.core.impl.RulingClass;
import org.algorithmx.rules.model.Severity;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.FormattedText;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Rule
public abstract class ValidationRule extends RulingClass {

    private Severity severity;
    private String errorMessage;
    private final String errorCode;

    protected ValidationRule(String errorCode) {
        this(errorCode, Severity.ERROR, null);
    }

    protected ValidationRule(String errorCode, Severity severity) {
        this(errorCode, severity, null);
    }

    protected ValidationRule(String errorCode, String errorMessage) {
        this(errorCode, Severity.ERROR, errorMessage);
    }

    protected ValidationRule(String errorCode, Severity severity, String errorMessage) {
        super();
        Assert.notNull(errorCode, "errorCode cannot be null.");
        Assert.notNull(severity, "severity cannot be null.");
        this.errorCode = errorCode;
        this.severity = severity;
        this.errorMessage = errorMessage;
    }

    @Otherwise
    public void otherwise(@Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) RuleContext ctx,
                          @Bind(using = BindingMatchingStrategyType.MATCH_BY_TYPE) ValidationErrorContainer errors) {
        Map<String, Binding> matches = resolveParameters(ctx);
        ValidationError error = createValidationError(getErrorCode(), getSeverity(), getErrorMessage(), matches);
        if (error != null) errors.add(error);
    }

    protected Map<String, Binding> resolveParameters(RuleContext ctx) {
        ParameterResolver.ParameterMatch[] matches = ctx.getParameterResolver().resolveAsBindings(
                getRuleDefinition().getCondition(), ctx.getBindings(), ctx.getMatchingStrategy());

        Map<String, Binding> result = new LinkedHashMap<>();

        if (matches != null) {
            for (ParameterResolver.ParameterMatch match : matches) {
                result.put(match.getDefinition().getName(), match.getBinding());
            }
        }

        return result;
    }

    protected ValidationError createValidationError(String errorCode, Severity severity, String errorMessage,
                                                    Map<String, Binding> matches) {
        ValidationError result = new ValidationError(getName(), errorCode, severity, formatErrorMessage(errorMessage, matches));
        addParameters(result, matches);
        return result;
    }

    protected String formatErrorMessage(String errorMessage, Map<String, Binding> matches) {
        FormattedText formattedText = FormattedText.parse(errorMessage);

        if (!formattedText.requiresFormatting()) return errorMessage;

        Map<String, Object> markers = new HashMap<>();

        if (matches != null) {
            for (Map.Entry<String, Binding> match : matches.entrySet()) {
                markers.put(match.getKey(), match.getValue() != null ? match.getValue().getValue() : null);
            }
        }

        return formattedText.format(markers);
    }

    protected void addParameters(ValidationError error, Map<String, Binding> matches) {
        Assert.notNull(error, "ValidationError cannot be null.");

        if (matches != null) {
            for (Map.Entry<String, Binding> match : matches.entrySet()) {
                error.param(match.getKey(), match.getValue() != null ? match.getValue().getTextValue() : null);
            }
        }
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

    public void setSeverity(Severity severity) {
        this.severity = severity;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
