package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.*;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

import static org.algorithmx.rules.util.RuleUtils.load;

public final class DefaultRuleFactory implements RuleFactory {

    private final ObjectFactory objectFactory;

    public DefaultRuleFactory(ObjectFactory objectFactory) {
        super();
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
    }

    public IdentifiableRule rule(RuleDefinition ruleDefinition) {
        return new SimpleRule(ruleDefinition, objectFactory.create(ruleDefinition.getRulingClass()));
    }
}
