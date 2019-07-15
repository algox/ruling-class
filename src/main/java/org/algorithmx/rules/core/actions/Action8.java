package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action8<A, B, C, D, E, F, G, H> extends Action {

    void then(A a, B b, C c, D d, E e, F f, G g, H h);
}
