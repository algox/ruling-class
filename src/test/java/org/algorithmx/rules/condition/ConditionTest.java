package org.algorithmx.rules.condition;

import org.algorithmx.rules.build.ConditionBuilder;
import org.algorithmx.rules.core.Condition;
import org.junit.Test;

import java.util.Date;

public class ConditionTest {

    public ConditionTest() {
        super();
    }

    @Test
    public void testCondition0() {
        Condition condition = ConditionBuilder.withNoArgs(() -> true).build();
        condition.isPass();
    }

    @Test
    public void testCondition2() {
        Condition condition = ConditionBuilder.with2Args((Integer a, String x) -> a > 55).build();
        condition.isPass(10, "abc");
    }

    @Test
    public void testCondition3() {
        Condition condition = ConditionBuilder.withThreeArgs((Integer a, Date date, String x) -> a != null).build();
        condition.isPass(10, new Date(), "abc");
    }
}
