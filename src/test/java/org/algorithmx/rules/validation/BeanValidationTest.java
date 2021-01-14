package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.function.FunctionBuilder;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.validation.rules.max.Max;
import org.junit.Assert;
import org.junit.Test;

public class BeanValidationTest {

    public BeanValidationTest() {
        super();
    }

    @Test
    public void test1() {
        Function<Rule> function = FunctionBuilder.with((@Max(12) Integer x) -> {
            Rule result = RuleBuilder.build(TestRule1.class);
            return result;
        }).build();
        
        Rule rule = RuleBuilder.with(TestRule1.class)
                //.preCondition(ConditionBuilder.build((@Max(12) Integer x) -> x > 10))
                .build();
        rule.getRuleDefinition().getConditionDefinition().getParameterDefinition(0).setName("x");
        RuleResult result = rule.run(x -> 1, errors -> new RuleViolations());
        Assert.assertTrue(result.getStatus().isPass());
    }
}
