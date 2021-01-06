package org.algorithmx.rules.core.registry;

import org.algorithmx.rules.core.rule.Rule;
import org.algorithmx.rules.core.ruleset.RuleSet;

public interface RuleRegistry {

    static RuleRegistry create() {
        return new DefaultRuleRegistry();
    }

    boolean isNameInUse(String name);

    int getRuleCount();

    int getRuleSetCount();

    Object get(String name);

    Rule[] getRules();

    RuleSet[] getRuleSets();

    <T> Rule<T> getRule(String name);

    RuleSet getRuleSet(String name);

    void register(Rule rule);

    void register(RuleSet rules);
}
