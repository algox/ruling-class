package org.algorithmx.rules.ruleflow;

import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.*;
import org.algorithmx.rules.core.impl.RuleFlowTemplate;
import org.junit.Test;

import java.math.BigDecimal;

public class SimpleRuleFlowTest {

    public SimpleRuleFlowTest() {
        super();
    }

    @Test
    public void simpleTest1() {
        Bindings bindings = Bindings.defaultBindings()
                .bind("y", String.class, "")
                .bind("a", String.class, "")
                .bind("b", String.class, "hello")
                .bind("c", Integer.class, 20)
                .bind("x", BigDecimal.class, new BigDecimal("100.00"));

        RuleFactory ruleFactory = RuleFactory.defaultFactory();

    }

    private static class RuleFlow1 extends RuleFlowTemplate<Integer> {

        public RuleFlow1(RuleExecutionContext ctx) {
            super(ctx);
        }

        @Override
        public Integer run() {
            int result = 0;


            return result;
        }

        /**@Override
        public Integer isPass() {
            int result = 0;

            if (isPass(rule("x")) && isPass(and(ruleSet("c")))) {
                result = 1;
            }

            return result;
        }**/
    }
}
