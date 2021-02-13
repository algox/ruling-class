package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.util.TimeComparator;
import org.junit.Assert;
import org.junit.Test;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;

public class TimeComparatorTest {

    public TimeComparatorTest() {
        super();
    }

    @Test
    public void test1() throws ParseException {
        Clock clock = Clock.systemDefaultZone();
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date pastDate = format.parse("1980-01-01");
        Date futureDate = format.parse("2180-01-01");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pastDate);
        Clock fixedPastClock = Clock.fixed(Instant.parse("1980-01-01T12:00:00.00Z"), ZoneId.of("America/Chicago"));
        Clock fixedFutureClock = Clock.fixed(Instant.parse("2180-01-01T12:00:00.00Z"), ZoneId.of("America/Chicago"));

        Assert.assertTrue(TimeComparator.compare(pastDate, clock) < 0);
        Assert.assertTrue(TimeComparator.compare(futureDate, clock) > 0);
        Assert.assertTrue(TimeComparator.compare(calendar, clock) < 0);
        Assert.assertTrue(TimeComparator.compare(fixedPastClock.instant(), clock) < 0);
        Assert.assertTrue(TimeComparator.compare(fixedFutureClock.instant(), clock) > 0);
        Assert.assertTrue(TimeComparator.compare(LocalDateTime.now(fixedPastClock), clock) < 0);
        Assert.assertTrue(TimeComparator.compare(LocalDateTime.now(fixedFutureClock), clock) > 0);
        Assert.assertTrue(TimeComparator.compare(LocalDate.now(fixedPastClock), clock) < 0);
        Assert.assertTrue(TimeComparator.compare(LocalDate.now(fixedFutureClock), clock) > 0);
        Assert.assertTrue(TimeComparator.compare(OffsetDateTime.now(fixedPastClock), clock) < 0);
        Assert.assertTrue(TimeComparator.compare(OffsetDateTime.now(fixedFutureClock), clock) > 0);
        Assert.assertTrue(TimeComparator.compare(YearMonth.now(fixedPastClock), clock) < 0);
        Assert.assertTrue(TimeComparator.compare(YearMonth.now(fixedFutureClock), clock) > 0);
        Assert.assertTrue(TimeComparator.compare(ZonedDateTime.now(fixedPastClock), clock) < 0);
        Assert.assertTrue(TimeComparator.compare(ZonedDateTime.now(fixedFutureClock), clock) > 0);
    }
}
