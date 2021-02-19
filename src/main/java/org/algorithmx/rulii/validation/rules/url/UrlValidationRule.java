package org.algorithmx.rulii.validation.rules.url;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.lib.apache.validation.RegexValidator;
import org.algorithmx.rulii.lib.apache.validation.UrlValidator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.util.Arrays;

/**
 * Validation Rule to make sure the the value must match a Url regex format.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must match a Url regex pattern.")
public class UrlValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.UrlValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} not a valid Url. Given {1}.";

    private String[] schemes;
    private String[] patterns;
    private UrlValidator validator;

    public UrlValidationRule(String bindingName, String[] schemes, String...patterns) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null, schemes, patterns);
    }

    public UrlValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage,
                             String[] schemes, String...patterns) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
        this.schemes = schemes;
        this.patterns = patterns;
        this.validator = new UrlValidator(schemes, patterns != null ? new RegexValidator(patterns) : null, 0L);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        if (!(value instanceof CharSequence))
            throw new ValidationRuleException("UrlValidationRule only applies to CharSequences(eg: Strings)."
                    + "Supplied Class [" + value.getClass() + "] value [" + value + "]");

        return validator.isValid(value.toString());
    }

    @Otherwise
    public void otherwise(RuleContext context, @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {
        Object value = getBindingValue(context);
        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("bindingName", getBindingName())
                .param(getBindingName(), value);
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
                ", bindingName=" + getBindingName() +
                '}';
    }
}
