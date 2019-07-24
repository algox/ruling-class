package org.algorithmx.rules.model;

import java.io.Serializable;

public interface Condition extends Serializable {

    long serialVersionUID = -0L;

    @FunctionalInterface
    interface Condition0 extends Condition {

        boolean when();
    }

    @FunctionalInterface
    interface Condition1<A> extends Condition {

        boolean when(A arg1);
    }

    @FunctionalInterface
    interface Condition2<A, B> extends Condition {

        boolean when(A arg1, B arg2);
    }

    @FunctionalInterface
    interface Condition3<A, B, C> extends Condition {

        boolean when(A arg1, B arg2, C arg3);
    }

    @FunctionalInterface
    interface Condition4<A, B, C, D> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4);
    }

    @FunctionalInterface
    interface Condition5<A, B, C, D, E> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4, E arg5);
    }

    @FunctionalInterface
    interface Condition6<A, B, C, D, E, F> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6);
    }

    @FunctionalInterface
    interface Condition7<A, B, C, D, E, F, G> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7);
    }

    @FunctionalInterface
    interface Condition8<A, B, C, D, E, F, G, H> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8);
    }

    @FunctionalInterface
    interface Condition9<A, B, C, D, E, F, G, H, I> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9);
    }

    @FunctionalInterface
    interface Condition10<A, B, C, D, E, F, G, H, I, J> extends Condition {

        boolean when(A arg1, B arg2, C arg3, D arg4, E arg5, F arg6, G arg7, H arg8, I arg9, J arg10);
    }
}
