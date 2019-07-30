package org.algorithmx.rules.core;

public interface IdentifiableRule extends BaseRule {

    String getName();

    default String getDescription() {
        return null;
    }
}
