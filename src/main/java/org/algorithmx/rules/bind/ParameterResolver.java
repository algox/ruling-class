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

import org.algorithmx.rules.error.UnrulyException;
import org.algorithmx.rules.bind.impl.DefaultParameterResolver;
import org.algorithmx.rules.model.MethodDefinition;

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
     * Resolves the method parameters into an array of Bindings. We use the matching strategy to resolves each parameter.
     *
     * @param definition method meta information.
     * @param bindings available bindings.
     * @param matchingStrategy matching strategy to use to resolve the bindings.
     * @return arrays of matching Bindings.
     * @throws UnrulyException if the Binding Strategy failed.
     */
    Binding<Object>[] resolveAsBindings(MethodDefinition definition, Bindings bindings,
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
        Binding<Object>[] bindingValues = resolveAsBindings(definition, bindings, matchingStrategy);

        if (bindingValues == null) return null;

        Object[] result = new Object[bindingValues.length];

        for (int i = 0; i < result.length; i++) {
            result[i] = bindingValues[i] != null ? bindingValues[i].getValue() : null;
        }

        return result;
    }
}
