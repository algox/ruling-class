package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action2<A, B> extends Action {

    void then(A a, B b);
}
