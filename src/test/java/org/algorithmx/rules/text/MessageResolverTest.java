package org.algorithmx.rules.text;

import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;

public class MessageResolverTest {

    public MessageResolverTest() {
        super();
    }

    @Test
    public void testMessageFormattedText1() {
        MessageResolver resolver = MessageResolver.create("message-resolver");
        String template = resolver.resolve(null, "test.001");
        Assert.assertTrue("test".equals(template));
    }

    @Test
    public void testMessageFormattedText2() {
        MessageResolver resolver = MessageResolver.create("message-resolver");
        String notFound = resolver.resolve(null, "unknown");
        Assert.assertNull(notFound);
    }

    @Test
    public void testMessageFormattedText3() {
        MessageResolver resolver = MessageResolver.create("message-resolver");
        String template = resolver.resolve(Locale.US, "test.001");
        Assert.assertTrue("locale specific test {0} ${b} ${c,number,int}".equals(template));
    }
}
