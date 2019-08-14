package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;

@FunctionalInterface
public interface Action {

    void run(RuleExecutionContext ctx) throws UnrulyException;

    default void run(BindingDeclaration... bindings) {
        run(Bindings.simpleBindings().bind(bindings));
    }

    default void run(Bindings bindings) throws UnrulyException {
        run(RuleExecutionContext.create(bindings));
    }

    static Then.Then0 arg0(Then.Then0 arg) {
        return arg;
    }

    static <A> Then.Then1 arg1(Then.Then1<A> arg) {
        return arg;
    }

    static <A, B> Then.Then2 arg2(Then.Then2<A, B> arg) {
        return arg;
    }

    static <A, B, C> Then.Then3 arg3(Then.Then3<A, B, C> arg) {
        return arg;
    }

    static <A, B, C, D> Then.Then4 arg4(Then.Then4<A, B, C, D> arg) {
        return arg;
    }

    static <A, B, C, D, E> Then.Then5 arg5(Then.Then5<A, B, C, D, E> arg) {
        return arg;
    }

    static <A, B, C, D, E, F> Then.Then6 arg6(Then.Then6<A, B, C, D, E, F> arg) {
        return arg;
    }

    static <A, B, C, D, E, F, G> Then.Then7 arg7(Then.Then7<A, B, C, D, E, F, G> arg) {
        return arg;
    }

    static <A, B, C, D, E, F, G, H> Then.Then8 arg8(Then.Then8<A, B, C, D, E, F, G, H> arg) {
        return arg;
    }

    static <A, B, C, D, E, F, G, H, I> Then.Then9 arg9(Then.Then9<A, B, C, D, E, F, G, H, I> arg) {
        return arg;
    }

    static <A, B, C, D, E, F, G, H, I, J> Then.Then10 arg10(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg) {
        return arg;
    }

}
