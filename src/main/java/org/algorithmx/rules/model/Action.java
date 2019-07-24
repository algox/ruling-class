package org.algorithmx.rules.model;

import java.io.Serializable;

public interface Action extends Serializable {

    long serialVersionUID = -0L;

    @FunctionalInterface
    interface Action0 extends Action {

        void then();
    }

    @FunctionalInterface
    interface Action1<A> extends Action {

        void then(A arg1);
    }

    @FunctionalInterface
    interface Action2<A, B> extends Action {

        void then(A arg1, B arg2);
    }

    @FunctionalInterface
    interface Action3<A, B, C> extends Action {

        void then(A arg1, B arg2, C arg3);
    }

    @FunctionalInterface
    interface Action4<A, B, C, D> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4);
    }

    @FunctionalInterface
    interface Action5<A, B, C, D, E> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4, E arg5);
    }

    @FunctionalInterface
    interface Action6<A, B, C, D, E, F> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6);
    }

    @FunctionalInterface
    interface Action7<A, B, C, D, E, F, G> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7);
    }

    @FunctionalInterface
    interface Action8<A, B, C, D, E, F, G, H> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8);
    }

    @FunctionalInterface
    interface Action9<A, B, C, D, E, F, G, H, I> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9);
    }

    @FunctionalInterface
    interface Action10<A, B, C, D, E, F, G, H, I, J> extends Action {

        void then(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9, J arg10);
    }
}
