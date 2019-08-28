package org.algorithmx.rules.action;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.util.RuleUtils;
import org.junit.Test;

public class RuleActionTest1 {

    public RuleActionTest1() {
        super();
    }

    @Test
    public void test1() {
        Bindings bindings = Bindings.simpleBindings();
        bindings.bind("x", String.class, "value");
        bindings.bind("y", Integer.class, 17);
        bindings.bind("z", Integer.class, 200);

        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        Rule ruleWithAction = ruleFactory.rule((String x, Integer y) -> y > 10)
                .then((Integer z) -> System.err.println("YASS! [" + z + "]"))
                .then((String x) -> System.err.println("MAN! [" + x + "]"));
        RuleEngine ruleEngine = RuleEngine.defaultRuleEngine();
        ruleEngine.run(ruleWithAction, bindings);

        RuleUtils.load((Condition.Condition3<Integer, String, Integer>) (a, b, c) -> a > 10, "test", "");
    }
}
