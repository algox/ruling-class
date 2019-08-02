package org.algorithmx.rules.core;

import org.algorithmx.rules.annotation.Managed;
import org.algorithmx.rules.bind.Bindings;

@FunctionalInterface
public interface RuleFlow<T> {

    T run();

    @Managed
    default Bindings bindings() {
        return null;
    }

    @Managed
    default boolean rule(Rule rule) {
        return false;
    }

    @Managed
    default boolean and(RuleSet ruleSet) {
        return and(ruleSet.getRules());
    }

    @Managed
    default boolean and(final Rule[] allRules) {
        return false;
    }

    @Managed
    default boolean or(RuleSet ruleSet) {
        return or(ruleSet.getRules());
    }

    @Managed
    default boolean or(final Rule[] allRules) {
        return false;
    }

    @Managed
    default boolean none(RuleSet ruleSet) {
        return none(ruleSet.getRules());
    }

    @Managed
    default boolean none(final Rule[] allRules) {
        return false;
    }
}
