package org.algorithmx.rulii.validation.rules.future;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.annotation.Match;
import org.algorithmx.rulii.annotation.Otherwise;
import org.algorithmx.rulii.annotation.Rule;
import org.algorithmx.rulii.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.util.TimeComparator;
import org.algorithmx.rulii.validation.BindingValidationRule;
import org.algorithmx.rulii.validation.RuleViolationBuilder;
import org.algorithmx.rulii.validation.RuleViolations;
import org.algorithmx.rulii.validation.Severity;
import org.algorithmx.rulii.validation.ValidationRuleException;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.util.Arrays;
import java.util.Calendar;

/**
 * Validation Rule to make sure the value is in the present or in the future.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be in the present or in the future.")
public class FutureOrPresentValidationRule extends BindingValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Calendar.class, Date.class, Date.class, Instant.class,
            LocalDate.class, LocalDateTime.class, LocalTime.class, MonthDay.class, OffsetDateTime.class,
            OffsetTime.class, Year.class, YearMonth.class, ZonedDateTime.class, HijrahDate.class, JapaneseDate.class,
            MinguoDate.class, ThaiBuddhistDate.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.FutureOrPresentValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "{0} must be in the present or in the future. Given {1}. Current clock {2}.";

    public FutureOrPresentValidationRule(String bindingName) {
        this(bindingName, ERROR_CODE, Severity.ERROR, null);
    }

    public FutureOrPresentValidationRule(String bindingName, String errorCode, Severity severity, String errorMessage) {
        super(bindingName, errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Override
    protected boolean isValid(RuleContext context, Object value) {
        if (value == null) return true;

        Integer result = TimeComparator.compare(value, context.getClock());

        if (result == null) {
            throw new ValidationRuleException("FutureOrPresentRule only applies to Date related classes. Supported Types ["
                    + Arrays.toString(SUPPORTED_TYPES) + "] Supplied Class [" + value.getClass() + "] value [" + value + "]");
        }

        return result >= 0;
    }

    @Otherwise
    public void otherwise(RuleContext context, @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {
        Object value = getBindingValue(context);
        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param("bindingName", getBindingName())
                .param(getBindingName(), value)
                .param("clock", context.getClock());
        errors.add(builder.build(context));
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Override
    public String toString() {
        return "FutureOrPresentValidationRule{" + "bindingName=" + getBindingName() + "}";
    }
}
