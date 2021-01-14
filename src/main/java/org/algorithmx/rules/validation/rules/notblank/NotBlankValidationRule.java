package org.algorithmx.rules.validation.rules.notblank;

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
 * Validation Rule to make sure the value is not blank (ie. it has some text).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value cannot be blank.")
public class NotBlankValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.NotBlankValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value cannot be blank.";

    public NotBlankValidationRule() {
        this(ERROR_CODE, Severity.ERROR, null);
    }

    public NotBlankValidationRule(String errorCode, Severity severity, String errorMessage) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("NotBlankValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "]");

        // Make sure there some text
        return value.toString().trim().length() > 0;
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
        return "NotBlankValidationRule{}";
    }
}
