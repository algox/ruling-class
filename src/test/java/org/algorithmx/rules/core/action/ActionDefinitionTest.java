package org.algorithmx.rules.core.action;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class ActionDefinitionTest {

    public ActionDefinitionTest() {
        super();
    }

    @Test
    public void test1() throws InvocationTargetException, IllegalAccessException {
        String x = "xxxxx";
        String y = "yyyyy";
        String z = "zzzzz";

        Action action = ActionBuilder.with((List<String> test) -> {
            System.err.println(x.substring(2));
            System.err.println(y.substring(2));
            System.err.println(z.trim());
            System.err.println(test.size());
        }).build();

    }
}
