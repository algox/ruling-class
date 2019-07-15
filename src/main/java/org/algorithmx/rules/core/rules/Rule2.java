package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule2<A, B> extends Rule {

    boolean when(A a, B b);
}
