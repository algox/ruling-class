package org.algorithmx.rules.core;

public interface IdentifiableRule extends Rule {

    String getName();

    default String getDescription() {
        return null;
    }
}
