package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action3<A, B, C> extends Action {

    void then(A a, B b, C c);
}
