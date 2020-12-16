package org.algorithmx.rules.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class MessageFormatterTest {

    public MessageFormatterTest() {
        super();
    }

    @Test
    public void testMessageFormatter1() {
        MessageFormatter formatter = MessageFormatter.create();
        String formatted = formatter.format(Locale.getDefault(), "this is a test");
        Assert.assertTrue(formatted.equals("this is a test"));
    }

    @Test
    public void testMessageFormatter2() {
        MessageFormatter formatter = MessageFormatter.create();
        ParameterInfo[] args = new ParameterInfo[3];
        args[0] = new ParameterInfo(0, "a", "oh");
        args[1] = new ParameterInfo(1, "b", "hello");
        args[2] = new ParameterInfo(2, "c", "there");
        String formatted = formatter.format(Locale.getDefault(), "Test: {0} {1} {2}", args);
        Assert.assertTrue(formatted.equals("Test: oh hello there"));
    }

    @Test
    public void testMessageFormatter3() {
        MessageFormatter formatter = MessageFormatter.create();
        ParameterInfo[] args = new ParameterInfo[2];
        args[0] = new ParameterInfo(0, "a", "oh");
        args[1] = new ParameterInfo(1, "b", 123);
        String formatted = formatter.format(Locale.getDefault(), "Test: {0} {1,number, integer }", args);
        Assert.assertTrue(formatted.equals("Test: oh 123"));
    }

    @Test
    public void testMessageFormatter4() {
        MessageFormatter formatter = MessageFormatter.create();
        ParameterInfo[] args = new ParameterInfo[2];
        args[0] = new ParameterInfo(0, "a", "oh");
        args[1] = new ParameterInfo(1, "b", 123);
        String formatted = formatter.format(Locale.getDefault(), "Test: ${a}  ${b,number, integer } ${a} ${b}", args);
        Assert.assertTrue(formatted.equals("Test: oh  123 oh 123"));
    }

    @Test
    public void testMessageFormatter5() {
        MessageFormatter formatter = MessageFormatter.create();
        ParameterInfo[] args = new ParameterInfo[2];
        args[0] = new ParameterInfo(0, "a", "oh");
        args[1] = new ParameterInfo(1, "b", 123);
        String formatted = formatter.format(Locale.getDefault(), "Test: ${a}  ${b,number, integer } ${a} ${b} {0} {1}", args);
        Assert.assertTrue(formatted.equals("Test: oh  123 oh 123 oh 123"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMessageFormatter6() {
        MessageFormatter formatter = MessageFormatter.create();
        ParameterInfo[] args = new ParameterInfo[2];
        args[0] = new ParameterInfo(0, "a", "oh");
        args[1] = new ParameterInfo(1, "b", 123);
        formatter.format(Locale.getDefault(), "Test: ${a}  ${b,number, integer ${a} ${b} {0} {1}", args);
    }
}
