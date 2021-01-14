package org.algorithmx.rules.validation.rules.url;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.apache.validation.RegexValidator;
import org.algorithmx.rules.lib.apache.validation.UrlValidator;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.validation.ValidationRuleException;

import java.util.Arrays;

/**
 * Validation Rule to make sure the the value must match a Url regex format.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must match a Url regex pattern.")
public class UrlValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.UrlValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value not a valid Url. Given {0}.";

    private String[] schemes;
    private String[] patterns;
    private UrlValidator validator;

    public UrlValidationRule(String[] schemes, String...patterns) {
        this(ERROR_CODE, Severity.ERROR, null, schemes, patterns);
    }

    public UrlValidationRule(String errorCode, Severity severity, String errorMessage, String[] schemes, String...patterns) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.schemes = schemes;
        this.patterns = patterns;
        this.validator = new UrlValidator(schemes, patterns != null ? new RegexValidator(patterns) : null, 0L);
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("UrlValidationRule only applies to CharSequences(eg: Strings)."
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

    public String[] getSchemes() {
        return schemes;
    }

    public String[] getPatterns() {
        return patterns;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "UrlValidationRule{" +
                "schemes=" + Arrays.toString(schemes) +
                ", patterns=" + Arrays.toString(patterns) +
                ", validator=" + validator +
                '}';
    }
}
