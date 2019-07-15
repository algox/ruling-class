package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action6<A, B, C, D, E, F> extends Action {

    void then(A a, B b, C c, D d, E e, F f);
}
