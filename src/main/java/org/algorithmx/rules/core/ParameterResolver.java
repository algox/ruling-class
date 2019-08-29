package org.algorithmx.rules.core;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.core.impl.DefaultParameterResolver;
import org.algorithmx.rules.model.MethodDefinition;

public interface ParameterResolver {

    static ParameterResolver defaultParameterResolver() {
        return new DefaultParameterResolver();
    }

    Binding<Object>[] resolveAsBindings(MethodDefinition definition, Bindings bindings,
                                        BindingMatchingStrategy matchingStrategy);

    default Object[] resolveAsBindingValues(MethodDefinition definition, Bindings bindings,
                                            BindingMatchingStrategy matchingStrategy) {
        Binding<Object>[] bindingValues = resolveAsBindings(definition, bindings, matchingStrategy);

        if (bindingValues == null) return null;

        Object[] result = new Object[bindingValues.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = bindingValues[i] != null ? bindingValues[i].getValue() : null;
        }

        return result;
    }
}
