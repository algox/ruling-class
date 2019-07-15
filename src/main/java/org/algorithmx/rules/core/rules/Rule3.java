package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule3<A, B, C> extends Rule {

    boolean when(A a, B b, C c);
}
