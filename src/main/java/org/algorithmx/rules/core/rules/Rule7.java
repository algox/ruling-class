package org.algorithmx.rules.core.rules;

@FunctionalInterface
public interface Rule7<A, B, C, D, E, F, G> extends Rule {

    boolean when(A a, B b, C c, D d, E e, F f, G g);
}
