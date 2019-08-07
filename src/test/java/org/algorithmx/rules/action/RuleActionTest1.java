package org.algorithmx.rules.action;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.Condition;
import org.algorithmx.rules.core.RuleAction;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.util.RuleUtils;
import org.junit.Test;

public class RuleActionTest1 {

    public RuleActionTest1() {
        super();
    }

    @Test
    public void test1() {
        Bindings bindings = Bindings.defaultBindings();
        bindings.bind("x", String.class, "value");
        bindings.bind("y", Integer.class, 17);
        bindings.bind("z", Integer.class, 200);

        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        RuleAction action = ruleFactory.rule("TestRule", (String x, Integer y) -> y > 10, "test")
                .and(ruleFactory.rule("TestRule1", (String x, Integer y) -> x.equals("value"), "test"))
                .then((Integer z) -> System.err.println("YASS! [" + z + "]"));
        action.run(bindings);

        RuleUtils.load((Condition.Condition3<Integer, String, Integer>) (a, b, c) -> a > 10, "test", "");
    }
}
