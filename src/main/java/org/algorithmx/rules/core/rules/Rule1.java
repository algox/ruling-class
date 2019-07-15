package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule1<A> extends Rule {

    boolean when(A a);
}
