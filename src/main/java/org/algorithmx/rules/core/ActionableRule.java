package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;

public interface ActionableRule extends Rule {

    default void run(RuleExecutionContext ctx) throws UnrulyException {
        if (isPass(ctx)) {
            for (Action action : getActions()) {
                action.run(ctx);
            }
        }
    }

    default void run(BindingDeclaration... bindings) {
        run(Bindings.simpleBindings().bind(bindings));
    }

    default void run(Bindings bindings) throws UnrulyException {
        run(RuleExecutionContext.create(bindings));
    }

    Action[] getActions();

    ActionableRule then(Action action);

    ActionableRule then(Then action);

    ActionableRule then(Then action, String description);

    ActionableRule then(Then.Then0 arg);

    <A> ActionableRule then(Then.Then1<A> arg);

    <A, B> ActionableRule then(Then.Then2<A, B> arg);

    <A, B, C> ActionableRule then(Then.Then3<A, B, C> arg);

    <A, B, C, D> ActionableRule then(Then.Then4<A, B, C, D> arg);

    <A, B, C, D, E> ActionableRule then(Then.Then5<A, B, C, D, E> arg);

    <A, B, C, D, E, F> ActionableRule then(Then.Then6<A, B, C, D, E, F> arg);

    <A, B, C, D, E, F, G> ActionableRule then(Then.Then7<A, B, C, D, E, F, G> arg);

    <A, B, C, D, E, F, G, H> ActionableRule then(Then.Then8<A, B, C, D, E, F, G, H> arg);

    <A, B, C, D, E, F, G, H, I> ActionableRule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg);

    <A, B, C, D, E, F, G, H, I, J> ActionableRule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg);
}
