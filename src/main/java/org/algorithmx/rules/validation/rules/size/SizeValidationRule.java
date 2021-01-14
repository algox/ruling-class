package org.algorithmx.rules.validation.rules.size;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.validation.RuleViolationBuilder;
import org.algorithmx.rules.validation.RuleViolations;
import org.algorithmx.rules.validation.Severity;
import org.algorithmx.rules.validation.SingleValueValidationRule;
import org.algorithmx.rules.validation.ValidationRuleException;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Validation Rule to make sure the size is between the given min and max values.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Size is between the given min and max values.")
public class SizeValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {boolean[].class, byte[].class, char[].class, double[].class, float[].class,
            int[].class, long[].class, short[].class, Object[].class, Collection.class, Map.class, CharSequence.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.SizeValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Size must be between {1} and {2}. Given {0}.";

    private final int min;
    private final int max;

    public SizeValidationRule(int min, int max) {
        this(ERROR_CODE, Severity.ERROR, null, min, max);
    }

    public SizeValidationRule(String errorCode, Severity severity, String errorMessage, int min, int max) {
        super(errorCode, severity, errorMessage);
        Assert.isTrue(min >= 0, "min >= 0");
        Assert.isTrue(max >= 0, "max >= 0");
        Assert.isTrue(max >= min, "max >= min");
        this.min = min;
        this.max = max;
    }

    @Given
    public boolean isValid(Object value) {
        if (value == null) return true;

        Integer size = null;

        if (value instanceof CharSequence) size = getSize((CharSequence) value);
        if (value instanceof Collection) size = getSize((Collection) value);
        if (value instanceof Map) size = getSize((Map) value);
        if (value.getClass().isArray()) size = getSize(value);

        if (size == null) {
            throw new ValidationRuleException("SizeValidationRule only applies to Collections/Maps/CharSequences and Arrays."
                    + "Supplied Class [" + value.getClass() + "]");
        }

        return size >= min && size <= max;
    }

    @Otherwise
    public void otherwise(RuleContext context, Object value,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), "size=" + getSize(value))
                .param("min", min)
                .param("max", max);

        errors.add(builder.build(context));
    }

    private int getSize(Object value) {
        return Array.getLength(value);
    }

    private int getSize(Collection collection) {
        return collection.size();
    }

    private int getSize(Map map) {
        return map.size();
    }

    private int getSize(CharSequence sequence) {
        return sequence.length();
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "SizeValidationRule{" +
                "min=" + min +
                ", max=" + max +
                '}';
    }
}
