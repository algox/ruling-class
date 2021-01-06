package org.algorithmx.rules.ruleset;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.PreCondition;
import org.algorithmx.rules.annotation.RuleSet;
import org.algorithmx.rules.annotation.Rules;
import org.algorithmx.rules.annotation.StopCondition;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.ruleset.RuleSetBuilder;

import java.math.BigDecimal;

@RuleSet(name = "TestRuleSet")
@Description("Sample Test RuleSet using a Class")
public class TestRuleSet {

    public TestRuleSet() {
        super();
    }

    @PreCondition
    public boolean preCondition() {
        return true;
    }

    @Rules
    public void load(RuleSetBuilder builder) {
        builder
                .action(ActionBuilder.build(() -> System.err.println("XXX Better Pre Action")))
                .rule(RuleBuilder
                        .with(ConditionBuilder.build((String y) -> y.equals("")))
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(0)))
                        .name("Rule1")
                        .build())
                .rule(RuleBuilder
                        .with(ConditionBuilder.build((String a, BigDecimal x) -> x != null))
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule2")
                        .build())
                .rule(RuleBuilder
                        .with(ConditionBuilder.build((String a, String b, Integer c) -> c == 20 && "hello".equals(b)))
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule3")
                        .build())
                .rule(RuleBuilder
                        .with(ConditionBuilder.TRUE())
                        .then(ActionBuilder.build((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule6")
                        .build())
                .action(ActionBuilder.build(() -> System.err.println("XXX Better Post Action")));
    }

    @StopCondition
    public boolean stopCondition() {
        return false;
    }

}
