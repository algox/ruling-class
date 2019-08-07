package org.algorithmx.rules.core;

public interface Identifiable {

    String getName();

    default String getDescription() {
        return null;
    }
}
