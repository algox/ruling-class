package org.algorithmx.rulii.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public final class NumberComparator {

    private static final Map<Class<?>, WholeNumberComparator<?>> numberComparators = new HashMap<>();
    private static final Map<Class<?>, DecimalComparator<?>> decimalComparators = new HashMap<>();
    private static final Map<Class<?>, NumberSignum<?>> signumComparators = new HashMap<>();

    static {
        numberComparators.put(Byte.class, (WholeNumberComparator<Byte>) NumberComparator::compareByte);
        numberComparators.put(byte.class, (WholeNumberComparator<Byte>) NumberComparator::compareByte);
        numberComparators.put(BigDecimal.class, (WholeNumberComparator<BigDecimal>) NumberComparator::compareBigDecimal);
        numberComparators.put(BigInteger.class, (WholeNumberComparator<BigInteger>) NumberComparator::compareBigInteger);
        numberComparators.put(Double.class, (WholeNumberComparator<Double>) NumberComparator::compareDouble);
        numberComparators.put(double.class, (WholeNumberComparator<Double>) NumberComparator::compareDouble);
        numberComparators.put(Float.class, (WholeNumberComparator<Float>) NumberComparator::compareFloat);
        numberComparators.put(float.class, (WholeNumberComparator<Float>) NumberComparator::compareFloat);
        numberComparators.put(Integer.class, (WholeNumberComparator<Integer>) NumberComparator::compareInteger);
        numberComparators.put(int.class, (WholeNumberComparator<Integer>) NumberComparator::compareInteger);
        numberComparators.put(Long.class, (WholeNumberComparator<Long>) NumberComparator::compareLong);
        numberComparators.put(long.class, (WholeNumberComparator<Long>) NumberComparator::compareLong);
        numberComparators.put(Short.class, (WholeNumberComparator<Short>) NumberComparator::compareShort);
        numberComparators.put(short.class, (WholeNumberComparator<Short>) NumberComparator::compareShort);

        decimalComparators.put(Byte.class, (DecimalComparator<Byte>) NumberComparator::compareByte);
        decimalComparators.put(byte.class, (DecimalComparator<Byte>) NumberComparator::compareByte);
        decimalComparators.put(BigDecimal.class, (DecimalComparator<BigDecimal>) NumberComparator::compareBigDecimal);
        decimalComparators.put(BigInteger.class, (DecimalComparator<BigInteger>) NumberComparator::compareBigInteger);
        decimalComparators.put(Double.class, (DecimalComparator<Double>) NumberComparator::compareDouble);
        decimalComparators.put(double.class, (DecimalComparator<Double>) NumberComparator::compareDouble);
        decimalComparators.put(Float.class, (DecimalComparator<Float>) NumberComparator::compareFloat);
        decimalComparators.put(float.class, (DecimalComparator<Float>) NumberComparator::compareFloat);
        decimalComparators.put(Integer.class, (DecimalComparator<Integer>) NumberComparator::compareInteger);
        decimalComparators.put(int.class, (DecimalComparator<Integer>) NumberComparator::compareInteger);
        decimalComparators.put(Long.class, (DecimalComparator<Long>) NumberComparator::compareLong);
        decimalComparators.put(long.class, (DecimalComparator<Long>) NumberComparator::compareLong);
        decimalComparators.put(Short.class, (DecimalComparator<Short>) NumberComparator::compareShort);
        decimalComparators.put(short.class, (DecimalComparator<Short>) NumberComparator::compareShort);

        signumComparators.put(Byte.class, (NumberSignum<Byte>) NumberComparator::signum);
        signumComparators.put(byte.class, (NumberSignum<Byte>) NumberComparator::signum);
        signumComparators.put(BigDecimal.class, (NumberSignum<BigDecimal>) NumberComparator::signum);
        signumComparators.put(BigInteger.class, (NumberSignum<BigInteger>) NumberComparator::signum);
        signumComparators.put(Double.class, (NumberSignum<Double>) NumberComparator::signum);
        signumComparators.put(double.class, (NumberSignum<Double>) NumberComparator::signum);
        signumComparators.put(Float.class, (NumberSignum<Float>) NumberComparator::signum);
        signumComparators.put(float.class, (NumberSignum<Float>) NumberComparator::signum);
        signumComparators.put(Integer.class, (NumberSignum<Integer>) NumberComparator::signum);
        signumComparators.put(int.class, (NumberSignum<Integer>) NumberComparator::signum);
        signumComparators.put(Long.class, (NumberSignum<Long>) NumberComparator::signum);
        signumComparators.put(long.class, (NumberSignum<Long>) NumberComparator::signum);
        signumComparators.put(Short.class, (NumberSignum<Short>) NumberComparator::signum);
        signumComparators.put(short.class, (NumberSignum<Short>) NumberComparator::signum);
    }

    private NumberComparator() {
        super();
    }

    public static Integer compare(Number number, long value) {
        WholeNumberComparator comparator = numberComparators.get(number.getClass());
        return comparator != null
                ? comparator.compare(number, value)
                : compareDouble(number.doubleValue(), value);
    }

    public static Integer compare(Number number, BigDecimal value) {
        DecimalComparator comparator = decimalComparators.get(number.getClass());
        return comparator != null
                ? comparator.compare(number, value)
                : compareDouble(number.doubleValue(), value);
    }

    public static Integer signum(Number number) {
        NumberSignum comparator = signumComparators.get(number.getClass());
        return comparator != null
                ? comparator.signum(number)
                : Long.signum(number.longValue());
    }

    public static int compare(long number, BigDecimal value) {
        return BigDecimal.valueOf(number).compareTo(value);
    }

    public static int compareByte(Byte number, long value) {
        return Long.compare(number, value);
    }

    public static int compareByte(Byte number, BigDecimal value) {
        return new BigDecimal(number).compareTo(value);
    }

    public static int compareBigDecimal(BigDecimal number, long value) {
        return number.compareTo(BigDecimal.valueOf(value));
    }

    public static int compareBigDecimal(BigDecimal number, BigDecimal value) {
        return number.compareTo(value);
    }

    public static int compareBigInteger(BigInteger number, long value) {
        return number.compareTo(BigInteger.valueOf(value));
    }

    public static int compareBigInteger(BigInteger number, BigDecimal value) {
        return new BigDecimal(number).compareTo(value);
    }

    public static Integer compareDouble(Double number, long value) {
        if (number.isNaN()) return null;
        if (number == Double.NEGATIVE_INFINITY) return -1;
        if (number == Double.POSITIVE_INFINITY) return 1;
        return Double.compare(number, value);
    }

    public static Integer compareDouble(Double number, BigDecimal value) {
        if (number.isNaN()) return null;
        if (number == Double.NEGATIVE_INFINITY) return -1;
        if (number == Double.POSITIVE_INFINITY) return 1;
        return BigDecimal.valueOf(number).compareTo(value);
    }

    public static Integer compareFloat(Float number, long value) {
        if (number.isNaN()) return null;
        if (number == Float.NEGATIVE_INFINITY) return -1;
        if (number == Float.POSITIVE_INFINITY) return 1;
        return Float.compare(number, value);
    }

    public static Integer compareFloat(Float number, BigDecimal value) {
        if (number.isNaN()) return null;
        if (number == Float.NEGATIVE_INFINITY) return -1;
        if (number == Float.POSITIVE_INFINITY) return 1;
        return BigDecimal.valueOf(number).compareTo(value);
    }

    public static int compareInteger(int number, long value) {
        return Long.compare(number, value);
    }

    public static int compareInteger(int number, BigDecimal value) {
        return compare(number, value);
    }

    public static int compareLong(long number, long value) {
        return Long.compare(number, value);
    }

    public static int compareLong(long number, BigDecimal value) {
        return compare(number, value);
    }

    public static int compareShort(short number, long value) {
        return Long.compare(number, value);
    }

    public static int compareShort(short number, BigDecimal value) {
        return compare(number, value);
    }

    public static int signum(Byte number) {
        return number.compareTo((byte) 0);
    }

    public static int signum(BigInteger number) {
        return number.signum();
    }

    public static int signum(BigDecimal number) {
        return number.signum();
    }

    public static Integer signum(Float number) {
        if (number.isNaN()) return null;
        if (number == Float.NEGATIVE_INFINITY) return -1;
        if (number == Float.POSITIVE_INFINITY) return 1;
        return number.compareTo(0F);
    }

    public static Integer signum(Double number) {
        if (number.isNaN()) return null;
        if (number == Double.NEGATIVE_INFINITY) return -1;
        if (number == Double.POSITIVE_INFINITY) return 1;
        return number.compareTo(0D);
    }

    public static int signum(Integer number) {
        return Integer.signum(number);
    }

    public static int signum(Long number) {
        return Long.signum( number );
    }

    public static int signum(Short number) {
        return number.compareTo((short) 0);
    }

    private interface WholeNumberComparator<T> {
        Integer compare(T value1, Long value2);
    }

    private interface DecimalComparator<T> {
        Integer compare(T value1, BigDecimal value2);
    }

    private interface NumberSignum<T> {
        Integer signum(T value);
    }
}
