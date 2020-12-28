package org.algorithmx.rules.validation;

import org.algorithmx.rules.annotation.Given;
import org.algorithmx.rules.annotation.Match;
import org.algorithmx.rules.annotation.Rule;
import org.algorithmx.rules.bind.match.MatchByTypeMatchingStrategy;
import org.algorithmx.rules.core.Identifiable;
import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.lib.spring.util.Assert;

@Rule
public class RuleProducingFunctionRule implements Identifiable {

    private final Function<?> supplier;
    private String ruleName;

    public RuleProducingFunctionRule(Function<?> supplier) {
        super();
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    @Given
    public boolean run(@Match(using = MatchByTypeMatchingStrategy.class) RuleContext context) {
        org.algorithmx.rules.core.rule.Rule rule = RuleBuilder.with(supplier.apply(context))
                .build();
        if (ruleName == null) this.ruleName = rule.getName();
        RuleResult result = rule.run(context);
        return result.getStatus().isPass();
    }

    @Override
    public String getName() {
        return ruleName != null ? ruleName : getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return "RuleProducingFunctionRule";
    }
}
