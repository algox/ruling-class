package org.algorithmx.rules.registry;

import org.algorithmx.rules.model.Condition;

public interface RuleRegistry {

    RuleRegistry register(Class<?> c);

    RuleRegistry register(String ruleName, Class<?> c);

    RuleRegistry register(String ruleName, Class<?> c, String description);

    RuleRegistry register(String name, Condition.Condition0 condition, String description);

    <A> RuleRegistry register(String name, Condition.Condition1<A> condition, String description);

    <A, B> RuleRegistry register(String name, Condition.Condition2<A, B> condition, String description);

    <A, B, C> RuleRegistry register(String name, Condition.Condition3<A, B, C> condition, String description);

    <A, B, C, D> RuleRegistry register(String name, Condition.Condition4<A, B, C, D> condition, String description);

    <A, B, C, D, E> RuleRegistry register(String name, Condition.Condition5<A, B, C, D, E> condition,
                                          String description);

    <A, B, C, D, E, F> RuleRegistry register(String name, Condition.Condition6<A, B, C, D, E, F> condition,
                                             String description);

    <A, B, C, D, E, F, G> RuleRegistry register(String name, Condition.Condition7<A, B, C, D, E, F, G> condition,
                                                String description);

    <A, B, C, D, E, F, G, H> RuleRegistry register(String name,
                                                   Condition.Condition8<A, B, C, D, E, F, G, H> condition,
                                                   String description);

    <A, B, C, D, E, F, G, H, I> RuleRegistry register(String name,
                                                      Condition.Condition9<A, B, C, D, E, F, G, H, I> condition,
                                                      String description);

    <A, B, C, D, E, F, G, H, I, J> RuleRegistry register(String name,
                                                         Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition,
                                                         String description);


}

