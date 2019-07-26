package org.algorithmx.rules.registry;

import org.algorithmx.rules.core.*;
import org.algorithmx.rules.registry.impl.DefaultRuleSet;

public interface RuleSet extends Iterable<Rule>, BaseRule {

    static RuleSet create(String name) {
        return new DefaultRuleSet(name);
    }

    String getName();

    Rule add(Class<?> c);

    Rule add(String name, Condition.Condition0 condition, String description);

    <A> Rule add(String name, Condition.Condition1<A> condition, String description);

    <A, B> Rule add(String name, Condition.Condition2<A, B> condition, String description);

    <A, B, C> Rule add(String name, Condition.Condition3<A, B, C> condition, String description);

    <A, B, C, D> Rule add(String name, Condition.Condition4<A, B, C, D> condition, String description);

    <A, B, C, D, E> Rule add(String name, Condition.Condition5<A, B, C, D, E> condition, String description);

    <A, B, C, D, E, F> Rule add(String name, Condition.Condition6<A, B, C, D, E, F> condition, String description);

    <A, B, C, D, E, F, G> Rule add(String name, Condition.Condition7<A, B, C, D, E, F, G> condition, String description);

    <A, B, C, D, E, F, G, H> Rule add(String name, Condition.Condition8<A, B, C, D, E, F, G, H> condition, String description);

    <A, B, C, D, E, F, G, H, I> Rule add(String name, Condition.Condition9<A, B, C, D, E, F, G, H, I> condition, String description);

    <A, B, C, D, E, F, G, H, I, J> Rule add(String name, Condition.Condition10<A, B, C, D, E, F, G, H, I, J> condition, String description);

    Rule get(String ruleName);
}

