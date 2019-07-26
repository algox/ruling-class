package org.algorithmx.rules.core;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.core.impl.SimpleRule;
import org.algorithmx.rules.model.RuleDefinition;

public interface Rule extends BaseRule {

    static Rule create(RuleDefinition ruleDefinition, ParameterResolver parameterResolver,
                       BindableMethodExecutor methodExecutor, ObjectFactory objectFactory) {
        return new SimpleRule(ruleDefinition, parameterResolver, methodExecutor, objectFactory);
    }

    boolean run(Object... args) throws UnrulyException;
}
