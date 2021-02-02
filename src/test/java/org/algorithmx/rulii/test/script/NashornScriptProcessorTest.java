/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.algorithmx.rulii.test.script;

import org.algorithmx.rulii.bind.BindingBuilder;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.action.ActionBuilder;
import org.algorithmx.rulii.core.condition.Condition;
import org.algorithmx.rulii.core.condition.ConditionBuilder;
import org.algorithmx.rulii.core.context.RuleContext;
import org.algorithmx.rulii.core.context.RuleContextBuilder;
import org.algorithmx.rulii.core.rule.Rule;
import org.algorithmx.rulii.core.rule.RuleBuilder;
import org.algorithmx.rulii.script.ScriptLanguageManager;
import org.algorithmx.rulii.script.ScriptProcessor;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

public class NashornScriptProcessorTest {

    public NashornScriptProcessorTest() {
        super();
    }

    @Test
    public void test1() {
        ScriptProcessor processor = ScriptLanguageManager.getScriptProcessor(ScriptProcessor.JAVASCRIPT);

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

        RuleContext context = RuleContextBuilder.build(bindings);
        Condition condition = ConditionBuilder.build("b >= 15");
        Assert.assertTrue(condition.isTrue(context));
    }

    @Test
    public void test3() {
        Bindings bindings = Bindings.create()
                .bind("a", "xxx")
                .bind("b", 15);

        RuleContext context = RuleContextBuilder.build(bindings);
        Rule rule = RuleBuilder
                        .name("TestRule")
                        .given(ConditionBuilder.build("b >= 15", ScriptProcessor.JAVASCRIPT))
                        .then(ActionBuilder.build("bindings.setValue('b', 20);"))
                        .otherwise(ActionBuilder.build("print('oh no')"))
                        .build();

        rule.run(context);
        Assert.assertTrue(bindings.getValue("b").equals(20));
    }
}
