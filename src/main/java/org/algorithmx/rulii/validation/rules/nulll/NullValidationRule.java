package org.algorithmx.rulii.validation.rules.nulll;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;

/**
 * Validation Rule to make sure the value is null.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be null.")
public class NullValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Object.class};

    public static final String ERROR_CODE      = "rulii.validation.rules.NullValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE = "{0} must be null. Given {1}.";

    public NullValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public NullValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        return value == null;
    }

    @Otherwise
    public void otherwise(RuleContext context, @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {
        Object value = getBindingValue(context);
        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("bindingName", getBindingName())
                .param(getBindingName(), value);
        errors.add(builder.build(context));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "NullValidationRule{" + ", bindingName=" + getBindingName() + "}";
    }
}
