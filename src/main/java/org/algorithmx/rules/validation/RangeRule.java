package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Otherwise;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleViolationBuilder;
import org.algorithmx.rules.core.rule.RuleViolations;
import org.algorithmx.rules.core.rule.Severity;
import org.algorithmx.rules.lib.spring.util.Assert;

import java.util.function.Supplier;

/**
 * Validation Rule to make sure the the value is in between the desired (min,max) range (inclusive).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value is in between the desired [min,max] range.")
public class RangeRule extends ValidationRule {

    private static final String ERROR_CODE        = "validators.rangeRule";
    private static final String DEFAULT_MESSAGE   = "Value must be greater than equal to {1} and less than equal {2}. Given {0}.";

    private final Number min;
    private final Number max;
    private final Supplier<Object> supplier;

    public RangeRule(Number min, Number max, Object value) {
        this(min, max, () -> value);
    }

    public RangeRule(Number min, Number max, Supplier<Object> supplier) {
        super(ERROR_CODE, Severity.ERROR, DEFAULT_MESSAGE);
        Assert.notNull(min, "min cannot be null.");
        Assert.notNull(max, "max cannot be null.");
        Assert.notNull(supplier, "supplier cannot be null.");
        this.min = min;
        this.max = max;
        this.supplier = supplier;
    }

    @Given
    public boolean isValid() {
        Object value = supplier.get();
        if (value == null) return false;
        return MinRule.isGreaterThanMin(value, min) && MaxRule.isLessThanMax(value, max);
    }

    @Otherwise
    public void otherwise(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("value", supplier.get())
                .param("min", min)
                .param("max", max);

        errors.add(builder.build(context));
    }

    public Number getMin() {
        return min;
    }

    public Number getMax() {
        return max;
    }

    public Supplier<Object> getSupplier() {
        return supplier;
    }

    @Override
    public String toString() {
        return "RangeRule{" +
                "min=" + min +
                ", max=" + max +
                ", value=" + supplier.get() +
                '}';
    }
}
