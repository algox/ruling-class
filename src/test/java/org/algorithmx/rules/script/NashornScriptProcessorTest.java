package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.BindingBuilder;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.action.ActionBuilder;
import org.algorithmx.rules.core.condition.Condition;
import org.algorithmx.rules.core.condition.ConditionBuilder;
import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.context.RuleContext;
import org.algorithmx.rules.core.context.RuleContextBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class NashornScriptProcessorTest {

    public NashornScriptProcessorTest() {
        super();
    }

    @Test
    public void test1() {
        ScriptProcessor processor = ScriptProcessor.create(ScriptProcessor.JAVASCRIPT);

        Bindings bindings = Bindings.create()
                .bind("a", "xxx")
                .bind("b", 15)
                .bind(BindingBuilder.with(key1 -> "hello world!").build())
                .bind(BindingBuilder.with(key2 -> 25).build())
                .bind(BindingBuilder.with(key3 -> new BigDecimal("100.00")).build());

        Object result = processor.evaluate("b > 10 && key2 > 20", bindings);
        Assert.assertTrue(result == Boolean.TRUE);
    }

    @Test
    public void test2() {
        Bindings bindings = Bindings.create()
                .bind("a", "xxx")
                .bind("b", 15);

        RuleContext context = RuleContextBuilder.create(bindings);
        Condition condition = ConditionBuilder.script("b >= 15");
        Assert.assertTrue(condition.isTrue(context));
    }

    @Test
    public void test3() {
        Bindings bindings = Bindings.create()
                .bind("a", "xxx")
                .bind("b", 15);

        RuleContext context = RuleContextBuilder.create(bindings);
        Rule rule = RuleBuilder
                        .with(ConditionBuilder.script("b >= 15"))
                        .then(ActionBuilder.script("bindings.setValue('b', 20);"))
                        .otherwise(ActionBuilder.script("print('oh no')"))
                        .name("TestRule")
                        .build();
        rule.run(context);
        Assert.assertTrue(bindings.getValue("b").equals(20));
    }
}
