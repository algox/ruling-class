package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.core.ObjectFactory;
import org.algorithmx.rules.core.Rule;
import org.algorithmx.rules.core.RuleFactory;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.spring.util.Assert;

public final class DefaultRuleFactory implements RuleFactory {

    private final ObjectFactory objectFactory;

    public DefaultRuleFactory(ObjectFactory objectFactory) {
        super();
        Assert.notNull(objectFactory, "objectFactory cannot be null.");
        this.objectFactory = objectFactory;
    }

    public Rule rule(RuleDefinition ruleDefinition) {
        return new SimpleRule(ruleDefinition,
                ruleDefinition.isStatic()
                        ? null
                        : objectFactory.create(ruleDefinition.getRulingClass()));
    }
}
