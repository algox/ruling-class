package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action5<A, B, C, D, E> extends Action {

    void then(A a, B b, C c, D d, E e);
}
