package org.algorithmx.rules.core.context;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.InvalidBindingException;
import org.junit.Test;

public class RuleContextTest {

    public RuleContextTest() {
        super();
    }

    @Test(expected = InvalidBindingException.class)
    public void test1() {
        RuleContext context = RuleContextBuilder.build(Bindings.create());
        context.getBindings().bind("context", 25);
    }
}
