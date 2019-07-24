package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.model.RuleDefinition;
import org.algorithmx.rules.core.impl.SimpleRule;

import java.util.function.Predicate;

public interface Rule extends Predicate<RuleExecutionContext> {

    static Rule create(RuleDefinition ruleDefinition, ParameterResolver parameterResolver,
                       BindableMethodExecutor methodExecutor, ObjectFactory objectFactory) {
        return new SimpleRule(ruleDefinition, parameterResolver, methodExecutor, objectFactory);
    }

    default boolean test(Bindings bindings) throws UnrulyException {
        return test(RuleExecutionContext.create(bindings));
    }

    boolean test(RuleExecutionContext ctx) throws UnrulyException;

    boolean run(Object...args) throws UnrulyException;

    RuleDefinition getRuleDefinition();
}
