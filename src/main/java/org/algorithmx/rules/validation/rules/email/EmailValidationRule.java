package org.algorithmx.rules.validation.rules.email;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.apache.validation.EmailValidator;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value must match an email regex format.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must match an email regex pattern.")
public class EmailValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.EmailValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value not a valid email address. Given {0}.";

    private final boolean allowLocal;
    private final boolean allowTopLevelDomain;
    private final EmailValidator validator;

    public EmailValidationRule(boolean allowLocal, boolean allowTopLevelDomain) {
        this(ERROR_CODE, Severity.ERROR, null, allowLocal, allowTopLevelDomain);
    }

    public EmailValidationRule(String errorCode, Severity severity, String errorMessage,
                               boolean allowLocal, boolean allowTopLevelDomain) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.allowLocal = allowLocal;
        this.allowTopLevelDomain = allowTopLevelDomain;
        this.validator = EmailValidator.getInstance(allowLocal, allowTopLevelDomain);
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("EmailValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return validator.isValid(value.toString());
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value);

        errors.add(builder.build(context));
    }

    public boolean isAllowLocal() {
        return allowLocal;
    }

    public boolean isAllowTopLevelDomain() {
        return allowTopLevelDomain;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "EmailValidationRule{" +
                "allowLocal=" + allowLocal +
                ", allowTopLevelDomain=" + allowTopLevelDomain +
                ", validator=" + validator +
                '}';
    }
}
