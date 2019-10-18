/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.algorithmx.rules.bind.impl;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.ParameterResolver;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.error.BindingException;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.ParameterDefinition;

import java.util.Set;

/**
 * Default Parameter Resolver implementation.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultParameterResolver implements ParameterResolver {

    public DefaultParameterResolver() {
        super();
    }

    @Override
    public ParameterMatch[] resolveAsBindings(MethodDefinition definition, Bindings ctx,
                                               BindingMatchingStrategy matchingStrategy) throws BindingException {
        ParameterMatch[] result = new ParameterMatch[definition.getParameterDefinitions().length];
        int index = 0;

        for (ParameterDefinition parameterDefinition : definition.getParameterDefinitions()) {
            // See if the parameter is overriding the matching strategy to be used.
            BindingMatchingStrategy matcher = parameterDefinition.getCustomMatchingStrategyType() != null
                    ? parameterDefinition.getCustomMatchingStrategyType().getStrategy()
                    : matchingStrategy;
            // Find all the matching bindings
            Set<Binding<Object>> bindings = matcher.match(ctx, parameterDefinition.getName(),
                    TypeReference.with(parameterDefinition.getType()));
            int matches = bindings.size();

            // Looks like we are missing a required parameter
            if (bindings.size() == 0 && parameterDefinition.isRequired()) {
                throw new BindingException(parameterDefinition, definition.getMethod(), bindings, matchingStrategy, ctx);
            } else if (matches == 0 && !parameterDefinition.isRequired()) {
                // Default to null
                result[index] = null;
            } else if (matches == 1) {
                Binding<Object> binding = bindings.stream().findFirst().get();

                if (parameterDefinition.isRequired() && binding.getValue() == null) {
                    throw new BindingException(parameterDefinition, definition.getMethod(), bindings, matchingStrategy, ctx);
                }

                // We found a match!
                result[index] = new ParameterMatch(parameterDefinition, binding);
            } else {
                // Too many matches found; cannot proceed.
                throw new BindingException("Found too many [" + matches + "]", parameterDefinition,
                        definition.getMethod(), bindings, matchingStrategy, ctx);
            }

            index++;
        }

        return result;
    }
}
