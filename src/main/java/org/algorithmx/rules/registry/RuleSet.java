package org.algorithmx.rules.registry;

import org.algorithmx.rules.model.Condition;

public interface RuleSet {

    RuleSet add(Class<?> c);

    RuleSet add(String name, Condition.Condition0 condition, String description);

    <A> RuleSet add(String name, Condition.Condition1<A> condition, String description);

    <A, B> RuleSet add(String name, Condition.Condition2<A, B> condition, String description);

    <A, B, C> RuleSet add(String name, Condition.Condition3<A, B, C> condition, String description);

    <A, B, C, D> RuleSet add(String name, Condition.Condition4<A, B, C, D> condition, String description);

    <A, B, C, D, E> RuleSet add(String name, Condition.Condition5<A, B, C, D, E> condition, String description);

    <A, B, C, D, E, F> RuleSet add(String name, Condition.Condition6<A, B, C, D, E, F> condition, String description);

    <A, B, C, D, E, F, G> RuleSet add(String name, Condition.Condition7<A, B, C, D, E, F, G> condition, String description);

    <A, B, C, D, E, F, G, H> RuleSet add(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition,
                                              String description);

    <A, B, C, D, E, F, G, H, I> RuleSet add(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition,
                                                 String description);

    <A, B, C, D, E, F, G, H, I, J> RuleSet add(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition,
                                                    String description);
}

