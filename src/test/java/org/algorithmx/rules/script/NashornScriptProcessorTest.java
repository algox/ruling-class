package org.algorithmx.rules.script;

import org.algorithmx.rules.bind.BindingBuilder;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.script.js.NashornScriptProcessor;
import org.junit.Test;

import java.math.BigDecimal;

public class NashornScriptProcessorTest {

    public NashornScriptProcessorTest() {
        super();
    }

    @Test
    public void test1() {
        ScriptProcessor processor = new NashornScriptProcessor();

        Bindings bindings = Bindings.create()
                .bind("a", "xxx")
                .bind("b", 15)
                .bind(BindingBuilder.with(key1 -> "hello world!").build())
                .bind(BindingBuilder.with(key2 -> 25).build())
                .bind(BindingBuilder.with(key3 -> new BigDecimal("100.00")).build());

        Object result = processor.evaluate("b > 10 && key2 > 30;", bindings);
        System.err.println(result);
    }
}
