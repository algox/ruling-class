package org.algorithmx.rules.validation;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.function.Function;
import org.algorithmx.rules.core.rule.RuleBuilder;
import org.algorithmx.rules.core.rule.RuleContext;
import org.algorithmx.rules.core.rule.RuleDefinition;
import org.algorithmx.rules.core.rule.RuleExecutionException;
import org.algorithmx.rules.core.rule.RuleResult;
import org.algorithmx.rules.core.rule.RulingClass;
import org.algorithmx.rules.lib.spring.util.Assert;

public class RuleProducingFunctionRule extends RulingClass {

    private final Function<?> supplier;
    private String ruleName;

    public RuleProducingFunctionRule(Function<?> supplier) {
        super(new RuleDefinition(RuleProducingFunctionRule.class, "RuleProducingFunctionRule", null));
        Assert.notNull(supplier, "supplier cannot be null.");
        this.supplier = supplier;
    }

    @Override
    public RuleResult run(RuleContext ctx) throws UnrulyException {
        Object target;
        boolean eventsEnabled = ctx.isEventsEnabled();

        try {
            ctx.setEventsEnabled(false);
            target = supplier.apply(ctx);
        } catch (Exception e) {
            throw new RuleExecutionException("Unable to create Rule using the given supplier.", e, this);
        } finally {
            ctx.setEventsEnabled(eventsEnabled);
        }

        org.algorithmx.rules.core.rule.Rule rule = RuleBuilder.create(target);
        if (ruleName == null) this.ruleName = rule.getName();

        return rule.run(ctx);
    }

    @Override
    public String getName() {
        return ruleName != null ? ruleName : getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getName();
    }
}
