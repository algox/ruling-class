package org.algorithmx.rulii.validation.rules.asssert;

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
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the value is false.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be false.")
public class AssertFalseValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {boolean.class, Boolean.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.AssertFalseValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be false.";

    public AssertFalseValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public AssertFalseValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof Boolean)) {
            throw new ValidationRuleException("AssertFalseValidationRule only applies to a boolean."
                    + "Supplied Class [" + value.getClass() + "]");
        }

        return Boolean.FALSE.equals(value);
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
        return "AssertFalseValidationRule{" + "bindingName=" + getBindingName() + "}";
    }
}
