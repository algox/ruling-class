package org.algorithmx.rulii.validation.rules.pattern;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.validation.RegexValidator;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

/**
 * Validation Rule to make sure the the value must match one of the given regex patterns.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must match the given regex pattern.")
public class PatternValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.PatternValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must match regex pattern {2}. Given {1}.";

    private final String pattern;
    private final boolean caseSensitive;
    private final RegexValidator validator;

    public PatternValidationRule(String bindingName, String pattern) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, true, pattern);
    }

    public PatternValidationRule(String bindingName, boolean caseSensitive, String pattern) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, caseSensitive, pattern);
    }

    public PatternValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage,
                                 boolean caseSensitive, String pattern) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        Assert.notNull(pattern, "pattern cannot be null.");
        this.pattern = pattern;
        this.caseSensitive = caseSensitive;
        this.validator = new RegexValidator(pattern, caseSensitive);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("PatternValidationRule only applies to CharSequences."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return validator.isValid(value.toString());
    }

    @Override
    protected void customizeViolation(RuleContext context, RuleViolationBuilder builder) {
        builder.param("pattern", pattern);
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    public String getPattern() {
        return pattern;
    }

    public boolean isCaseSensitive() {
        return caseSensitive;
    }

    @Override
    public String toString() {
        return "PatternValidationRule{" +
                "pattern=" + pattern +
                ", caseSensitive=" + caseSensitive +
                ", bindingName=" + getBindingName() +
                '}';
    }
}
