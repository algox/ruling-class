package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.DefaultParameterResolver;
import org.algorithmx.rules.model.MethodDefinition;

public interface ParameterResolver {

    static ParameterResolver defaultParameterResolver() {
        return new DefaultParameterResolver();
    }

    Object[] resolve(MethodDefinition definition, Bindings bindings, BindingMatchingStrategy matchingStrategy);
}
