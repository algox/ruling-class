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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.bind.impl.DefaultParameterResolver;
import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.model.MethodDefinition;
import org.algorithmx.rules.model.ParameterDefinition;
import org.algorithmx.rules.spring.util.Assert;

/**
 * Resolves a method's parameters from the given Bindings and a MatchingStrategy.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface ParameterResolver {

    /**
     * Creates a the default implementation of the ParameterResolver.
     *
     * @return a new instance of the ParameterResolver.
     */
    static ParameterResolver defaultParameterResolver() {
        return new DefaultParameterResolver();
    }

    /**
     * Resolves the method parameters into an array of matches (binding + parameter defn).
     * We use the matching strategy to resolves each parameter.
     *
     * @param definition method meta information.
     * @param bindings available bindings.
     * @param matchingStrategy matching strategy to use to resolve the bindings.
     * @return arrays of matches.
     * @throws UnrulyException if the Binding Strategy failed.
     */
    ParameterMatch[] resolveAsBindings(MethodDefinition definition, Bindings bindings,
                                        BindingMatchingStrategy matchingStrategy) throws UnrulyException;

    /**
     * Resolves the method parameters into an array of values. We use the matching strategy to resolves each parameter.
     *
     * @param definition method meta information.
     * @param bindings available bindings.
     * @param matchingStrategy matching strategy to use to resolve the bindings.
     * @return arrays of method parameter values.
     * @throws UnrulyException if the Binding Strategy failed.
     */
    default Object[] resolveAsBindingValues(MethodDefinition definition, Bindings bindings,
                                            BindingMatchingStrategy matchingStrategy) throws UnrulyException {
        ParameterMatch[] matches = resolveAsBindings(definition, bindings, matchingStrategy);
        return resolveAsBindingValues(matches);
    }

    /**
     * Resolves the parameter matches to values.
     *
     * @param matches parameter matches.
     * @return resulting values.
     */
    default Object[] resolveAsBindingValues(ParameterMatch[] matches) throws UnrulyException {
        if (matches == null) return null;

        Object[] result = new Object[matches.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = matches[i] != null
                    ? (matches[i].definition.isBinding() ? matches[i].binding : matches[i].binding.getValue())
                    : null;
        }

        return result;
    }

    /**
     * Stores the parameter with its corresponding matched binding.
     */
    class ParameterMatch {
        private final ParameterDefinition definition;
        private final Binding<Object> binding;

        public ParameterMatch(ParameterDefinition definition, Binding<Object> binding) {
            super();
            Assert.notNull(definition, "definition cannot be null.");
            Assert.notNull(binding, "binding cannot be null.");
            this.definition = definition;
            this.binding = binding;
        }

        public ParameterDefinition getDefinition() {
            return definition;
        }

        public Binding<Object> getBinding() {
            return binding;
        }
    }
}
