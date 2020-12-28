package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleViolationBuilder;
import org.algorithmx.rules.core.rule.RuleViolations;
import org.algorithmx.rules.core.rule.Severity;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Validation Rule to make sure the the value is greater than the desired Min.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is greater than the desired Min.")
public class MinRule extends ValidationRule {

    private static final String ERROR_CODE      = "validators.minRule";
    private static final String DEFAULT_MESSAGE = "Value must be greater than/equal to {1}. Given {0}.";

    private final Number min;
    private final Supplier<Object> supplier;

    public MinRule(Number min, Object value) {
        this(min, () -> value);
    }

    public MinRule(Number min, Supplier<Object> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(min, "min cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.min = min;
        this.supplier = supplier;
    }

    @Given
    public boolean isValid() {
        Object value = supplier.get();
        if (value == null) return false;
        return isGreaterThanMin(value, min);
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("value", supplier.get())
                .param("min", min);

        errors.add(builder.build(context));
    }

    /**
     * Determines if the given object (size/length) is greater than or equal to the Min value.
     *
     * @param value given Object.
     * @param min minimum size.
     * @return given object (size/length) greater than the size of the Object.
     */
    public static boolean isGreaterThanMin(Object value, Number min) {
        if (value == null) return false;

        if (value instanceof Number) {
            Number number = (Number) value;
            return number.doubleValue() >= min.doubleValue();
        } else if (value instanceof CharSequence) {
            CharSequence text = (CharSequence) value;
            return text.length() >= min.intValue();
        } else if (value instanceof Collection) {
            Collection collection = (Collection) value;
            return collection.size() >= min.intValue();
        } else if (value instanceof Map) {
            Map map = (Map) value;
            return map.size() >= min.intValue();
        } else if (value.getClass().isArray()) {
            return Array.getLength(value) >= min.intValue();
        }

        throw new UnrulyException("MinRule is not supported on type [" + value.getClass()
                + "] only supported on numbers, string, collections, maps and arrays");
    }

    public Number getMin() {
        return min;
    }

    public Supplier<Object> getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return "MinRule{" +
                "min=" + min +
                ", value=" + supplier.get() +
                '}';
    }
}
