/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2019, Live Software & Consultants Inc (rules@algorithmx.org)
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
package org.algorithmx.rules.bind.match;

import org.algorithmx.rules.bind.BindingException;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.convert.string.ConverterRegistry;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.util.reflect.ObjectFactory;

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
    static ParameterResolver create() {
        return new DefaultParameterResolver();
    }

    /**
     * Matches the method parameters to an array of Bindings. We use the matching strategy to resolves each parameter.
     *
     * @param definition method meta information.
     * @param bindings available bindings.
     * @param matchingStrategy matching strategy to use to resolve the bindings.
     * @param objectFactory factory that to be used to create custom BindingStrategies.
     * @return arrays of matches.
     * @throws UnrulyException if the Binding Strategy failed.
     */
    ParameterMatch[] match(MethodDefinition definition, Bindings bindings,
                                       BindingMatchingStrategy matchingStrategy, ObjectFactory objectFactory) throws BindingException;

    /**
     * Resolves the parameter matches to actual values.
     *
     * @param matches parameter matches.
     * @param definition method meta information.
     * @param bindings available bindings.
     * @param matchingStrategy matching strategy to use to resolve the bindings.
     * @param registry converter registry.
     * @return resulting values.
     */
    Object[] resolve(ParameterMatch[] matches, MethodDefinition definition, Bindings bindings,
                     BindingMatchingStrategy matchingStrategy, ConverterRegistry registry) throws BindingException;
}
