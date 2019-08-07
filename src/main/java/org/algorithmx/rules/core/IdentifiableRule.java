package org.algorithmx.rules.core;

import org.algorithmx.rules.model.RuleDefinition;

public interface IdentifiableRule extends Rule, Identifiable {

    RuleDefinition getRuleDefinition();

    @Override
    default String getName() {
        return getRuleDefinition().getName();
    }

    @Override
    default String getDescription() {
        return getRuleDefinition().getDescription();
    }
}
