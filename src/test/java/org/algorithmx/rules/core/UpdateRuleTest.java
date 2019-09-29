package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.Bindings;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.algorithmx.rules.core.Conditions.*;
import static org.algorithmx.rules.core.Actions.act1;

public class UpdateRuleTest {

    public UpdateRuleTest() {
        super();
    }

    @Test @Ignore
    public void test1() {
        Bindings bindings = Bindings.defaultBindings()
                .bind("x", int.class, 11)
                .bind("y", int.class, 0);

        RuleFactory ruleFactory = RuleFactory.defaultFactory();
        Rule rule = ruleFactory.rule(cond2((Integer x, List<Integer> z) -> x > 10))
                .then(act1((Binding<Integer> y) -> y.setValue(100)));
        rule.run(bindings);

        System.err.println((int) bindings.get("y"));
    }
}
