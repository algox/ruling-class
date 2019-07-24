package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.UnrulyException;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.core.ParameterResolver;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.ParameterDefinition;

import java.util.Set;

public class DefaultParameterResolver implements ParameterResolver {

    public DefaultParameterResolver() {
        super();
    }

    @Override
    public Object[] resolve(MethodDefinition definition, Bindings ctx, BindingMatchingStrategy matchingStrategy) {
        Object[] result = new Object[definition.getParameterDefinitions().length];
        int index = 0;

        for (ParameterDefinition parameterDefinition : definition.getParameterDefinitions()) {
            // Find all the matching bindings
            Set<Binding<Object>> bindings = matchingStrategy.match(ctx, parameterDefinition.getName(),
                    TypeReference.with(parameterDefinition.getType()));
            int matches = bindings.size();

            // Looks like we are missing a required parameter
            if (bindings.size() == 0 && parameterDefinition.isRequired()) {
                throw new UnrulyException("Unable to find a matching Binding for param ["
                        + parameterDefinition.getName() + "] on method [" + definition.getMethod() + "]");

            } else if (matches == 0 && !parameterDefinition.isRequired()) {
                // Default to null
                result[index] = null;
            } else if (matches == 1) {
                Binding<Object> binding = bindings.stream().findFirst().get();

                if (parameterDefinition.isRequired() && binding.getValue() == null) {
                    throw new UnrulyException("Missing required param value for [" + parameterDefinition.getName()
                            + "] method [" + definition.getMethod() + "]. Binding used [" + binding + "]");
                }

                // We found a match!
                result[index] = binding.getValue();
            } else {
                // Too many matches found; cannot proceed.
                throw new UnrulyException("Found too many [" + matches + "] Binding matches for param ["
                        + parameterDefinition.getName() + "] on method [" + definition.getMethod() + "]. Matches found ["
                        + bindings.toString() + "] using BindingMatchingStrategy ["
                        + matchingStrategy.getClass().getSimpleName() + "]");
            }

            index++;
        }

        return result;
    }
}
