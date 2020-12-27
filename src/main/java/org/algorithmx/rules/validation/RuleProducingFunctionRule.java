package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.NoTrace;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.lib.spring.util.Assert;

@Rule
@NoTrace
public class RuleProducingFunctionRule {

    private final Function<?> supplier;

    public RuleProducingFunctionRule(Function<?> supplier) {
        super();
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    @Given
    public boolean run(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context) {
        RuleBuilder.with(supplier.apply(context))
                .build()
                .run(context);
        return true;
    }

    @Override
    public String toString() {
        return "RuleProducingFunctionRule{" +
                "supplier=" + supplier.getTarget() +
                '}';
    }
}
