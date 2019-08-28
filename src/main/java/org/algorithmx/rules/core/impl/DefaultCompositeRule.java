package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.CompositeRule;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleExecutionContext;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

import java.util.function.BiPredicate;

public class DefaultCompositeRule extends RuleTemplate implements CompositeRule {

    private final Rule[] rules;
    private final RuleDefinition ruleDefinition;
    private final BiPredicate<Rule[], RuleExecutionContext> test;

    public DefaultCompositeRule(Rule[] rules, BiPredicate<Rule[], RuleExecutionContext> test) {
        super();
        Assert.notNull(rules, "rules cannot be null");
        Assert.notNull(test, "test cannot be null");
        Assert.isTrue(rules.length > 0, " there must at least one rule");
        this.rules = rules;
        this.test = test;
        this.ruleDefinition = new RuleDefinition(getClass(), "CompositeRule", null, null);
    }

    @Override
    public boolean isPass(RuleExecutionContext ctx) throws UnrulyException {
        return test.test(rules, ctx);
    }

    @Override
    public final Rule[] getRules() {
        return rules;
    }

    @Override
    public Object getTarget() {
        return this;
    }

    @Override
    public RuleDefinition getRuleDefinition() {
        return ruleDefinition;
    }
}
