package org.algorithmx.rules.util;

import java.time.Clock;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public final class TimeComparator {

    private static final Map<Class<?>, TemporalComparator<?>> comparators = new HashMap<>();

    static {
        comparators.put(Calendar.class, (TemporalComparator<Calendar>) TimeComparator::compare);
        comparators.put(Date.class, (TemporalComparator<Date>) TimeComparator::compare);
        comparators.put(java.sql.Date.class, (TemporalComparator<java.sql.Date>) TimeComparator::compare);
        comparators.put(Instant.class, (TemporalComparator<Instant>) TimeComparator::compare);
        comparators.put(LocalDate.class, (TemporalComparator<LocalDate>) TimeComparator::compare);
        comparators.put(LocalDateTime.class, (TemporalComparator<LocalDateTime>) TimeComparator::compare);
        comparators.put(LocalTime.class, (TemporalComparator<LocalTime>) TimeComparator::compare);
        comparators.put(MonthDay.class, (TemporalComparator<MonthDay>) TimeComparator::compare);
        comparators.put(OffsetDateTime.class, (TemporalComparator<OffsetDateTime>) TimeComparator::compare);
        comparators.put(OffsetTime.class, (TemporalComparator<OffsetTime>) TimeComparator::compare);
        comparators.put(Year.class, (TemporalComparator<Year>) TimeComparator::compare);
        comparators.put(YearMonth.class, (TemporalComparator<YearMonth>) TimeComparator::compare);
        comparators.put(ZonedDateTime.class, (TemporalComparator<ZonedDateTime>) TimeComparator::compare);
        comparators.put(HijrahDate.class, (TemporalComparator<HijrahDate>) TimeComparator::compare);
        comparators.put(JapaneseDate.class, (TemporalComparator<JapaneseDate>) TimeComparator::compare);
        comparators.put(MinguoDate.class, (TemporalComparator<MinguoDate>) TimeComparator::compare);
        comparators.put(ThaiBuddhistDate.class, (TemporalComparator<ThaiBuddhistDate>) TimeComparator::compare);
    }

    private TimeComparator() {
        super();
    }

    public static Integer compare(Object value, Clock clock) {
        TemporalComparator comparator = comparators.get(value.getClass());

        if (comparator != null) return comparator.compare(value, clock);
        if (value instanceof Date) return compare((Date) value, clock);
        if (value instanceof Calendar) return compare((Calendar) value, clock);

        // Could not find a comparator
        return null;
    }

    public static int compare(Calendar value, Clock clock) {
        return value.toInstant().compareTo(clock.instant());
    }

    public static int compare(Date value, Clock clock) {
        return value.toInstant().compareTo(clock.instant());
    }

    public static int compare(java.sql.Date value, Clock clock) {
        return value.toInstant().compareTo(clock.instant());
    }

    public static int compare(Instant value, Clock clock) {
        return value.compareTo(clock.instant());
    }

    public static int compare(LocalDate value, Clock clock) {
        return value.compareTo(LocalDate.now(clock));
    }

    public static int compare(LocalDateTime value, Clock clock) {
        return value.compareTo(LocalDateTime.now(clock));
    }

    public static int compare(LocalTime value, Clock clock) {
        return value.compareTo(LocalTime.now(clock));
    }

    public static int compare(OffsetDateTime value, Clock clock) {
        return value.compareTo(OffsetDateTime.now(clock));
    }

    public static int compare(OffsetTime value, Clock clock) {
        return value.compareTo(OffsetTime.now(clock));
    }

    public static int compare(MonthDay value, Clock clock) {
        return value.compareTo(MonthDay.now(clock));
    }

    public static int compare(Year value, Clock clock) {
        return value.compareTo(Year.now(clock));
    }

    public static int compare(YearMonth value, Clock clock) {
        return value.compareTo(YearMonth.now(clock));
    }

    public static int compare(ZonedDateTime value, Clock clock) {
        return value.compareTo(ZonedDateTime.now(clock));
    }

    public static int compare(HijrahDate value, Clock clock) {
        return value.compareTo(HijrahDate.now(clock));
    }

    public static int compare(JapaneseDate value, Clock clock) {
        return value.compareTo(JapaneseDate.now(clock));
    }

    public static int compare(MinguoDate value, Clock clock) {
        return value.compareTo(MinguoDate.now(clock));
    }

    public static int compare(ThaiBuddhistDate value, Clock clock) {
        return value.compareTo(ThaiBuddhistDate.now(clock));
    }

    private interface TemporalComparator<T> {
        int compare(T value, Clock clock);
    }
}
