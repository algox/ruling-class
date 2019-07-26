package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.CompositeRule;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.core.RuleSet;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public class DefaultRuleFactory implements RuleFactory {

    public DefaultRuleFactory() {
        super();
    }

    @Override
    public CompositeRule all(RuleSet ruleSet) {
        return CompositeRule.all(createRule(ruleSet));
    }

    @Override
    public CompositeRule any(RuleSet ruleSet) {
       return CompositeRule.any(createRule(ruleSet));
    }

    @Override
    public CompositeRule none(RuleSet ruleSet) {
        return CompositeRule.none(createRule(ruleSet));
    }

    protected Rule[] createRule(RuleSet ruleSet) {
        Assert.notNull(ruleSet, "ruleSet cannot be null");

        Rule[] result = new Rule[ruleSet.size()];
        int index = 0;

        for (RuleDefinition definition : ruleSet) {
            result[index++] = Rule.create(definition);
        }

        return result;
    }
}
