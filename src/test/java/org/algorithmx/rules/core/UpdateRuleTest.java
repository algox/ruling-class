package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.Bindings;
import org.junit.Assert;
import org.junit.Test;

import static org.algorithmx.rules.core.Actions.act1;
import static org.algorithmx.rules.core.Conditions.cond1;

public class UpdateRuleTest {

    public UpdateRuleTest() {
        super();
    }

    @Test
    public void test1() {
        Bindings binds = Bindings.defaultBindings()
                .bind("x", int.class, 11)
                .bind("y", int.class, 10);

        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        Rule rule = ruleFactory.rule(cond1((Integer x) -> x > 10))
                .then(act1((Bindings bindings) -> bindings.set("y", 100)));
        rule.run(binds);

        Assert.assertTrue((int) binds.get("y") == 100);
    }
}
