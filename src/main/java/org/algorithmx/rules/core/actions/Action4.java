package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action4<A, B, C, D> extends Action {

    void then(A a, B b, C c, D d);
}
