package org.algorithmx.rules.core.actions;

@FunctionalInterface
public interface Action1<A> extends Action {

    void then(A a);
}
