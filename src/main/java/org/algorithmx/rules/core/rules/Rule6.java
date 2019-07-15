package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule6<A, B, C, D, E, F> extends Rule {

    boolean when(A a, B b, C c, D d, E e, F f);
}
