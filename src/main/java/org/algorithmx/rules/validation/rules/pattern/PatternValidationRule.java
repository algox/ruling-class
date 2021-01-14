package org.algorithmx.rules.validation.rules.pattern;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.apache.validation.RegexValidator;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.validation.ValidationRuleException;

import java.util.Arrays;

/**
 * Validation Rule to make sure the the value must match one of the given regex patterns.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must match one of the given regex pattern(s).")
public class PatternValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.PatternValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must match one of the regex patterns {1}. Given {0}.";

    private final String[] patterns;
    private final boolean caseSensitive;
    private final RegexValidator validator;

    public PatternValidationRule(String...patterns) {
        this(ERROR_CODE, Severity.ERROR, null, true, patterns);
    }

    public PatternValidationRule(boolean caseSensitive, String...patterns) {
        this(ERROR_CODE, Severity.ERROR, null, caseSensitive, patterns);
    }

    public PatternValidationRule(String errorCode, Severity severity, String errorMessage,
                                 boolean caseSensitive, String...patterns) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(patterns, "patterns cannot be null.");
        Assert.isTrue(patterns.length > 0, "must have at least one pattern.");
        this.patterns = patterns;
        this.caseSensitive = caseSensitive;
        this.validator = new RegexValidator(patterns, caseSensitive);
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("PatternValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return validator.isValid(value.toString());
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value)
                .param("patterns", Arrays.toString(patterns));

        errors.add(builder.build(context));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public String[] getPatterns() {
        return patterns;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public String toString() {
        return "PatternValidationRule{" +
                "patterns=" + Arrays.toString(patterns) +
                ", caseSensitive=" + caseSensitive +
                ", validator=" + validator +
                '}';
    }
}
