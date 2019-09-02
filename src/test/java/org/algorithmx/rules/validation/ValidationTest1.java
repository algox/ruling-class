package org.algorithmx.rules.validation;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.RuleEngine;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.model.ValidationErrorContainer;
import org.junit.Before;
import org.junit.Test;

public class ValidationTest1 {

    private RuleFactory ruleFactory;
    private RuleEngine ruleEngine;

    public ValidationTest1() {
        super();
    }

    @Before
    public void init() {
        this.ruleFactory = RuleFactory.defaultFactory();
        this.ruleEngine = RuleEngine.defaultRuleEngine();
    }

    @Test
    public void test1() {
        RuleSet rules = ruleFactory.rules("RuleSet1", "Test Rule Set")
                .add(TestRule1.class)
                .add(TestRule2.class)
                .add(TestRule3.class)
                .add(TestRule4.class)
                .add(TestRule5.class);

        Bindings bindings = Bindings.defaultBindings()
                .bind("value", Integer.class, 1)
                .bind("errors", ValidationErrorContainer.class, new ValidationErrorContainer());

        ruleEngine.run(rules, bindings);
        System.err.println(bindings.get("errors").toString());
    }

    @Test
    public void test2() {
        RuleSet rules = ruleFactory.rules("RuleSet2", "Test Rule Set")
                .add(TestRule1.class)
                .add(TestRule2.class)
                .add(TestRule3.class)
                .add(TestRule4.class)
                .add(TestRule5.class);

        RuleExecutionContext ctx = ruleEngine.run(rules, value -> 75, errors -> new ValidationErrorContainer());
        System.err.println(ctx.bindings().get("errors").toString());
    }
}
