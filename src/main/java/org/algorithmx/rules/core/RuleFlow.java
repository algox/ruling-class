package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.Bindings;

@FunctionalInterface
public interface RuleFlow<T> {

    T run();

    default Bindings bindings() {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean rule(String name) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default RuleSet ruleSet(String name) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean run(Rule rule) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean all(String ruleSet) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean all(final Rule[] allRules) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean any(String ruleSet) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean any(final Rule[] allRules) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean none(String ruleSet) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }

    default boolean none(final Rule[] allRules) {
        throw new UnrulyException("RuleFlow must be executed in a managed environment.");
    }
}
