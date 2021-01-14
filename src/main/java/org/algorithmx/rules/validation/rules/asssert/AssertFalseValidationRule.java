package org.algorithmx.rules.validation.rules.asssert;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the value is false.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be false.")
public class AssertFalseValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {boolean.class, Boolean.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.AssertFalseValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be false.";

    public AssertFalseValidationRule() {
        this(ERROR_CODE, Severity.ERROR, null);
    }

    public AssertFalseValidationRule(String errorCode, Severity severity, String errorMessage) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        if (!(value instanceof Boolean)) {
            throw new ValidationRuleException("AssertFalseValidationRule only applies to a boolean."
                    + "Supplied Class [" + value.getClass() + "]");
        }

        return Boolean.FALSE.equals(value);
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value);

        errors.add(builder.build(context));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "AssertFalseValidationRule{}";
    }
}
