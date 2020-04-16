/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.bind.convert;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Converter tests.
 *
 * @author Max Arulananthan
 */

public class ConverterTest {

    public ConverterTest() {
        super();
    }

    @Test(expected = ConversionException.class)
    public void stringToBigDecimalTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, BigDecimal> converter = registry.find(String.class, BigDecimal.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, BigDecimal.class));
        Assert.assertTrue(converter.convert("100.004").equals(new BigDecimal("100.004")));
        converter.convert("xxxx");
    }

    @Test(expected = ConversionException.class)
    public void stringToBigIntegerTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, BigInteger> converter = registry.find(String.class, BigInteger.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, BigInteger.class));
        Assert.assertTrue(converter.convert("11223423435456546").equals(new BigInteger("11223423435456546")));
        converter.convert("xxxxx");
    }

    @Test
    public void stringToBooleanTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Boolean> converter = registry.find(String.class, Boolean.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Boolean.class));
        Assert.assertTrue(converter.convert("y").equals(true));
        Assert.assertTrue(converter.convert("true").equals(true));
        Assert.assertTrue(converter.convert("false").equals(false));
        Assert.assertTrue(converter.convert("").equals(false));
    }

    @Test(expected = ConversionException.class)
    public void stringToByteTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Byte> converter = registry.find(String.class, Byte.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Byte.class));
        byte b = 123;
        Assert.assertTrue(converter.convert("123").equals(b));
        converter.convert("xxxx");
    }

    @Test(expected = ConversionException.class)
    public void stringToDoubleTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Double> converter = registry.find(String.class, Double.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Double.class));
        Assert.assertTrue(converter.convert("123.45555").equals(123.45555));
        converter.convert("xxxx");
    }

    @Test(expected = ConversionException.class)
    public void stringToFloatTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Float> converter = registry.find(String.class, Float.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Float.class));
        Assert.assertTrue(converter.convert("123.455").equals(123.455f));
        converter.convert("xxxx");
    }

    @Test(expected = ConversionException.class)
    public void stringToIntegerTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Integer> converter = registry.find(String.class, Integer.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Integer.class));
        Assert.assertTrue(converter.convert("123455").equals(123455));
        Assert.assertTrue(converter.convert("123455.50").equals(123456));
        converter.convert("xxxx");
    }

    @Test(expected = ConversionException.class)
    public void stringToLongTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Long> converter = registry.find(String.class, Long.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Long.class));
        Assert.assertTrue(converter.convert("1234553892374").equals(1234553892374l));
        Assert.assertTrue(converter.convert("1234553892374.50").equals(1234553892375l));
        converter.convert("xxxx");
    }

    @Test(expected = ConversionException.class)
    public void stringToShortTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, Short> converter = registry.find(String.class, Short.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, Short.class));
        Assert.assertTrue(converter.convert("64001").equals(64001));
        converter.convert("xxxx");
    }

    @Test
    public void stringToStringTest() {
        ConverterRegistry registry = ConverterRegistry.create();
        Converter<String, String> converter = registry.find(String.class, String.class);
        Assert.assertTrue(converter != null);
        Assert.assertTrue(converter.canConvert(String.class, String.class));
        Assert.assertTrue(converter.convert("xxxxx").equals("xxxxx"));
    }
}
