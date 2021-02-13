package org.algorithmx.rulii.test.validation;

import org.algorithmx.rulii.util.NumberComparator;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.atomic.AtomicLong;

public class NumberComparatorTest {

    public NumberComparatorTest() {
        super();
    }

    @Test
    public void test1() {
        Byte b = 12;
        BigDecimal bigDecimal = new BigDecimal("1243345353.77");
        BigInteger bigInteger = new BigInteger("127372846238426377");
        Float f = 12.23f;
        Double d = 15.22;
        int i = 23432;
        long l = 7667453l;
        short s = 123;
        double nan = Math.sqrt(-1);
        AtomicLong atomicLong = new AtomicLong(23549823478l);

        Assert.assertTrue(NumberComparator.compare(b, 0) > 0);
        Assert.assertTrue(NumberComparator.compare(bigDecimal, 1000) > 0);
        Assert.assertTrue(NumberComparator.compare(bigInteger, 1000) > 0);
        Assert.assertTrue(NumberComparator.compare(f, 1000) < 0);
        Assert.assertTrue(NumberComparator.compare(d, 1000) < 0);
        Assert.assertTrue(NumberComparator.compare(d, 16) < 0);
        Assert.assertTrue(NumberComparator.compare(i, 23432) == 0);
        Assert.assertTrue(NumberComparator.compare(l, Long.MAX_VALUE) < 0);
        Assert.assertTrue(NumberComparator.compare(s, Long.MIN_VALUE) > 0);
        Assert.assertTrue(NumberComparator.compare(nan, 0) == null);

        Assert.assertTrue(NumberComparator.compare(b, new BigDecimal(0)) > 0);
        Assert.assertTrue(NumberComparator.compare(bigInteger, new BigDecimal(1000.00)) > 0);
        Assert.assertTrue(NumberComparator.compare(f, new BigDecimal(1000.00)) < 0);
        Assert.assertTrue(NumberComparator.compare(d, new BigDecimal(1000.01)) < 0);
        Assert.assertTrue(NumberComparator.compare(d, new BigDecimal(16.0343)) < 0);
        Assert.assertTrue(NumberComparator.compare(i, new BigDecimal(23432.00)) == 0);
        Assert.assertTrue(NumberComparator.compare(l, new BigDecimal(Long.MAX_VALUE)) < 0);
        Assert.assertTrue(NumberComparator.compare(s, new BigDecimal(Long.MIN_VALUE)) > 0);
        Assert.assertTrue(NumberComparator.compare(nan, new BigDecimal(0)) == null);
        Assert.assertTrue(NumberComparator.compare(atomicLong, 1000) > 0);
    }
}
