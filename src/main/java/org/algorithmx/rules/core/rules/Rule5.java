package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule5<A, B, C, D, E> extends Rule {

    boolean when(A a, B b, C c, D d, E e);
}
