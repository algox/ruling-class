package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.Bindings;
import org.junit.Test;

public class UpdateRuleTest {

    public UpdateRuleTest() {
        super();
    }

    @Test
    public void test1() {
        Bindings binds = Bindings.defaultBindings()
                .bind("x", int.class, 11)
                .bind("y", int.class, 10);

        // TODO : Fix (Binding<Integer> y
        /*Rule rule = ruleFactory.rule(cond1((Integer x) -> x > 10))
                .then(act1((Binding<Integer> y) -> y.setValue(100)));
        rule.run(binds);

        Assert.assertTrue((int) binds.get("y") == 100);*/
    }
}
