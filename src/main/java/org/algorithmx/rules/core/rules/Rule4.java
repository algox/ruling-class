package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule4<A, B, C, D> extends Rule {

    boolean when(A a, B b, C c, D d);
}
