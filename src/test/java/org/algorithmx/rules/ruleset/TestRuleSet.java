package org.algorithmx.rules.ruleset;

import org.algorithmx.rules.annotation.Description;
import org.algorithmx.rules.annotation.ErrorCondition;
import org.algorithmx.rules.annotation.PostAction;
import org.algorithmx.rules.annotation.PreAction;
import org.algorithmx.rules.annotation.PreCondition;
import org.algorithmx.rules.annotation.RuleSet;
import org.algorithmx.rules.annotation.Rules;
import org.algorithmx.rules.annotation.StopCondition;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    @PreAction
    public void preAction(int c) {
        //System.err.println("Pre Action [" + c + "]");
    }

    @Rules
    public Rule[] getRules() {

        Rule rule1 = RuleBuilder
                        .with(ConditionBuilder.create((String y) -> y.equals("")))
                        .then(ActionBuilder.create((Binding<Integer> c) -> c.setValue(0)))
                        .name("Rule1")
                        .build();
        Rule rule2 = RuleBuilder
                        .with(ConditionBuilder.create((String a, BigDecimal x) -> x != null))
                        .then(ActionBuilder.create((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule2")
                        .build();
        Rule rule3 = RuleBuilder
                        .with(ConditionBuilder.create((String a, String b, Integer c) -> c == 20 && "hello".equals(b)))
                        .then(ActionBuilder.create((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule3")
                        .build();
        Rule rule6 = RuleBuilder
                        .with(ConditionBuilder.TRUE())
                        .then(ActionBuilder.create((Binding<Integer> c) -> c.setValue(c.getValue() + 1)))
                        .name("Rule6")
                        .build();

        List<Rule> result = new ArrayList<>();

        result.add(rule1);
        result.add(rule2);
        result.add(rule3);
        result.add(rule6);

        return result.toArray(new Rule[result.size()]);

    }

    @StopCondition
    public boolean stopCondition() {
        return false;
    }

    @ErrorCondition
    public boolean errorCondition() {
        return false;
    }

    @PostAction
    public void postAction(int c) {
        //System.err.println("Post Action [" + c + "]");
    }

}
