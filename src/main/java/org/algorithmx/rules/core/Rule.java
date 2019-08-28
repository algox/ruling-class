package org.algorithmx.rules.core;

import org.algorithmx.rules.model.RuleDefinition;

import java.util.function.Predicate;

public interface Rule extends Predicate<Object[]> {

    boolean isPass(Object...args) throws UnrulyException;

    default boolean isFail(Object...args) throws UnrulyException {
        return !isPass(args);
    }

    default boolean test(Object...args) throws UnrulyException {
        return isPass(args);
    }

    default boolean isIdentifiable() {
        return this instanceof Identifiable;
    }

    Object getTarget();

    RuleDefinition getRuleDefinition();

    Action[] getActions();

    Rule then(Action action);

    Rule then(Then action);

    Rule then(Then action, String description);

    Rule then(Then.Then0 arg);

    <A> Rule then(Then.Then1<A> arg);

    <A, B> Rule then(Then.Then2<A, B> arg);

    <A, B, C> Rule then(Then.Then3<A, B, C> arg);

    <A, B, C, D> Rule then(Then.Then4<A, B, C, D> arg);

    <A, B, C, D, E> Rule then(Then.Then5<A, B, C, D, E> arg);

    <A, B, C, D, E, F> Rule then(Then.Then6<A, B, C, D, E, F> arg);

    <A, B, C, D, E, F, G> Rule then(Then.Then7<A, B, C, D, E, F, G> arg);

    <A, B, C, D, E, F, G, H> Rule then(Then.Then8<A, B, C, D, E, F, G, H> arg);

    <A, B, C, D, E, F, G, H, I> Rule then(Then.Then9<A, B, C, D, E, F, G, H, I> arg);

    <A, B, C, D, E, F, G, H, I, J> Rule then(Then.Then10<A, B, C, D, E, F, G, H, I, J> arg);

}
