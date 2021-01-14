package org.algorithmx.rules.validation.rules.past;

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
import org.algorithmx.rules.util.TimeComparator;
import org.algorithmx.rules.validation.ValidationRuleException;

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
 * Validation Rule to make sure the value is in the past.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
@Rule
@Description("Value must be in the past.")
public class PastValidationRule extends SingleValueValidationRule {

    public static Class<?>[] SUPPORTED_TYPES    = {Calendar.class, Date.class, Date.class, Instant.class,
            LocalDate.class, LocalDateTime.class, LocalTime.class, MonthDay.class, OffsetDateTime.class,
            OffsetTime.class, Year.class, YearMonth.class, ZonedDateTime.class, HijrahDate.class, JapaneseDate.class,
            MinguoDate.class, ThaiBuddhistDate.class};

    public static final String ERROR_CODE       = "rulii.validation.rules.PastValidationRule.errorCode";
    public static final String DEFAULT_MESSAGE  = "Value must be in the past. Given {0}. Current clock {1}.";

    public PastValidationRule() {
        this(ERROR_CODE, Severity.ERROR, null);
    }

    public PastValidationRule(String errorCode, Severity severity, String errorMessage) {
        super(errorCode, severity, errorMessage, DEFAULT_MESSAGE);
    }

    @Given
    public boolean isValid(Object value, RuleContext context) {
        if (value == null) return true;

        Integer result = TimeComparator.compare(value, context.getClock());

        if (result == null) {
            throw new ValidationRuleException("PastValidationRule only applies to Date related classes. Like ["
                    + Arrays.toString(SUPPORTED_TYPES) + "] Supplied Class [" + value.getClass() + "] value [" + value + "]");
        }

        return result < 0;
    }

    @Override
    public Class<?>[] getSupportedTypes() {
        return SUPPORTED_TYPES;
    }

    @Otherwise
    public void otherwise(Object value, RuleContext context,
                          @Match(using = MatchByTypeMatchingStrategy.class) RuleViolations errors) {

        RuleViolationBuilder builder = createRuleViolationBuilder()
                .param(getRuleDefinition().getConditionDefinition().getParameterDefinitions()[0].getName(), value)
                .param("clock", context.getClock());

        errors.add(builder.build(context));
    }

    @Override
    public String toString() {
        return "PastValidationRule{}";
    }
}
