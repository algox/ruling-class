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

    void run(RuleSet rule, RuleExecutionContext ctx) throws UnrulyException;

    default RuleExecutionContext run(Rule rule, BindingDeclaration...bindings) {
        RuleExecutionContext result = RuleExecutionContext.create(Bindings.defaultBindings().bind(bindings));
        run(rule, result);
        return result;
    }

    default RuleExecutionContext run(Rule rule, Bindings bindings) throws UnrulyException {
        RuleExecutionContext result = RuleExecutionContext.create(bindings);
        run(rule, result);
        return result;
    }

    default RuleExecutionContext run(RuleSet rule, BindingDeclaration...bindings) {
        RuleExecutionContext result = RuleExecutionContext.create(Bindings.defaultBindings().bind(bindings));
        run(rule, result);
        return result;
    }

    default RuleExecutionContext run(RuleSet rule, Bindings bindings) throws UnrulyException {
        RuleExecutionContext result = RuleExecutionContext.create(bindings);
        run(rule, result);
        return result;
    }
}
