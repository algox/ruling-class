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
package org.algorithmx.rules.core.impl;

import org.algorithmx.rules.RuntimeRuleException;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.core.BindableMethodExecutor;
import org.algorithmx.rules.core.MethodDefinition;
import org.algorithmx.rules.core.ParameterDefinition;
import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.spring.util.Assert;

import java.lang.reflect.Modifier;
import java.util.Arrays;

/**
 * Default Implementation of BindableMethodExecutor.
 *
 * @author Max Arulananthan
 * @since 1.0
 * @see BindableMethodExecutor
 */
public class DefaultBindableMethodExecutor implements BindableMethodExecutor {

    public DefaultBindableMethodExecutor() {
        super();
    }

    /**
     * Executes the BindableMethod and returns its result. Following the steps below:
     *
     * <li>First resolve all the parameters (as per the MethodDefinition) and Bind values to them using the given matching strategy.</li>
     * <li>Validate and make sure all required params are met</li>
     * <li>Execute the BindableMethod with the derived args from above</li>
     *
     * @param target target object.
     * @param definition method details.
     * @param ctx all Bindings to use.
     * @param matchingStrategy strategy used to match Bindings to method parameters.
     * @param <T> response type.
     * @return result of the method execution.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T execute(Object target, MethodDefinition definition, Bindings ctx, BindingMatchingStrategy matchingStrategy) {
        Assert.notNull(definition, "definition cannot be null");
        Assert.notNull(ctx, "bind cannot be null");
        Assert.notNull(matchingStrategy, "matchingStrategy cannot be null");

        boolean staticMethod = Modifier.isStatic(definition.getMethod().getModifiers());
        int index = 0;

        Object[] args = new Object[staticMethod
                ? definition.getParameterDefinitions().length
                : definition.getParameterDefinitions().length + 1];

        if (!staticMethod) args[index++] = target;

        for (ParameterDefinition parameterDefinition : definition.getParameterDefinitions()) {
            // Find all the matching bindings
            Binding<?>[] bindings = matchingStrategy.match(ctx, parameterDefinition.getName(),
                    TypeReference.with(parameterDefinition.getType()));

            // Looks like we are missing a required parameter
            if (bindings.length == 0 && parameterDefinition.isRequired()) {
                throw new RuntimeRuleException("Unable to find a matching Binding for param ["
                        + parameterDefinition.getName() + "] on method [" + definition.getMethod() + "]");

            } else if (bindings.length == 0 && !parameterDefinition.isRequired()) {
                // Default to null
                args[index] = null;
            } else if (bindings.length == 1 && bindings[0].getValue() == null && parameterDefinition.isRequired()) {
                throw new RuntimeRuleException("Missing required param value for [" + parameterDefinition.getName()
                        + "] method [" + definition.getMethod() + "]. Binding used [" + bindings[0] + "]");
            } else if (bindings.length == 1) {
                // We found a match!
                args[index] = bindings[0].getValue();
            } else {
                // Too many matches found; cannot proceed.
                throw new RuntimeRuleException("Found too many [" + bindings.length + "] Binding matches for param ["
                        + parameterDefinition.getName() + "] on method [" + definition.getMethod() + "]. Matches found ["
                        + Arrays.toString(bindings) + "] using BindingMatchingStrategy ["
                        + matchingStrategy.getClass().getSimpleName() + "]");
            }

            index++;
        }

        try {
            // Execute the method with the derived parameters
            return (T) definition.getMethodHandle().invokeWithArguments(args);
        } catch (Throwable e) {
            // Something went wrong with the execution
            throw new RuntimeRuleException("Error trying to execute [" + definition.getMethod()
                    + "] with arguments [" + Arrays.toString(args) + "]", e);
        }
    }
}
