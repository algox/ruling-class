package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingDeclaration;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.DefaultRuleEngine;

public interface RuleEngine {

    static RuleEngine defaultRuleEngine() {
        return new DefaultRuleEngine();
    }

    default String getName() {
        return getClass().getSimpleName();
    }

    void run(Rule rule, RuleExecutionContext ctx) throws UnrulyException;

    default void run(Rule rule, BindingDeclaration...bindings) {
        run(rule, Bindings.simpleBindings().bind(bindings));
    }

    default void run(Rule rule, Bindings bindings) throws UnrulyException {
        run(rule, RuleExecutionContext.create(bindings));
    }

    void run(RuleSet rule, RuleExecutionContext ctx) throws UnrulyException;

    default void run(RuleSet rule, BindingDeclaration...bindings) {
        run(rule, Bindings.simpleBindings().bind(bindings));
    }

    default void run(RuleSet rule, Bindings bindings) throws UnrulyException {
        run(rule, RuleExecutionContext.create(bindings));
    }
}
