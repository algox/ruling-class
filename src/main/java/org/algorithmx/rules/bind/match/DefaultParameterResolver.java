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

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingException;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.convert.Converter;
import org.algorithmx.rules.bind.convert.ConverterRegistry;
import org.algorithmx.rules.core.UnrulyException;
import org.algorithmx.rules.core.model.MethodDefinition;
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.util.TypeReference;
import org.algorithmx.rules.util.reflect.ObjectFactory;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.util.Map;

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
    public ParameterMatch[] match(MethodDefinition definition, Bindings bindings,
                                              BindingMatchingStrategy matchingStrategy,
                                              ObjectFactory objectFactory) throws BindingException {
        ParameterMatch[] result = new ParameterMatch[definition.getParameterDefinitions().length];
        int index = 0;

        for (ParameterDefinition parameterDefinition : definition.getParameterDefinitions()) {
            BindingMatchingStrategy matcher = matchingStrategy;

            result[index] = null;

            // See if the parameter is overriding the matching strategy to be used.
            if (parameterDefinition.isBindSpecified()) {
                matcher = objectFactory.create(parameterDefinition.getBindUsing());
            }

            boolean isBinding = parameterDefinition.isBinding();
            // Find all the matching bindings
            Map<String, Binding<Object>> matches = matcher.match(bindings, parameterDefinition.getName(),
                    TypeReference.with(isBinding
                            ? parameterDefinition.getBindingType()
                            : parameterDefinition.getType()));
            int matchesCount = matches.size();

            if (matchesCount == 0) {
                result[index] = new ParameterMatch(parameterDefinition, null, isBinding);
            } else if (matchesCount == 1) {
                Binding<Object> binding = matches.values().stream().findFirst().get();
                result[index] = new ParameterMatch(parameterDefinition, binding, isBinding);
            } else {
                // More than one match found; let's see if there is a primary candidate
                Binding<Object> primaryBinding = null;

                for (Binding<Object> binding : matches.values()) {
                    if (binding.isPrimary()) {
                        primaryBinding = binding;
                        break;
                    }
                }

                if (primaryBinding != null) {
                    result[index] = new ParameterMatch(parameterDefinition, primaryBinding, isBinding);
                } else {
                    // Too many matches found; cannot proceed.
                    throw new BindingException("Multiple Bindings found using ("
                            + matchingStrategy.getClass().getSimpleName() + "). Perhaps specify a primary Binding? ",
                            definition, parameterDefinition, matches, bindings);
                }
            }

            index++;
        }

        return result;
    }

    @Override
    public Object[] resolve(ParameterMatch[] matches, MethodDefinition definition, Bindings bindings,
                            BindingMatchingStrategy matchingStrategy, ConverterRegistry registry) throws BindingException {
        if (matches == null) return null;

        Object[] result = new Object[matches.length];

        for (int i = 0; i < result.length; i++) {

            if (matches[i] == null) {
                throw new UnrulyException("Invalid state. You cannot have a null match");
            }

            Object value;

            // There was no match; let's see if there is default value
            if (matches[i].getBinding() == null) {
                if (matches[i].getDefinition().getDefaultValue() != null) {
                    Converter<String, ?> converter = registry.find(String.class, matches[i].getDefinition().getType());

                    if (converter == null) {
                        throw new BindingException("Cannot find a converter that will convert default value ["
                                + matches[i].getDefinition().getDefaultValue() + "] to type ["
                                + matches[i].getDefinition().getType() + "]", definition, matches[i].getDefinition(),
                                null, bindings);
                    }

                    value = converter.convert(matches[i].getDefinition().getDefaultValue());
                } else if (!matches[i].getDefinition().isRequired()) {
                     value = ReflectionUtils.getDefaultValue(matches[i].getDefinition().getType());
                } else {
                    throw new BindingException("No matching Binding found using (" + matchingStrategy.getClass().getSimpleName()
                            + ")", definition, matches[i].getDefinition(), null, bindings);
                }
            } else {
                value = matches[i].isBinding()
                        ? matches[i].getBinding()
                        : matches[i].getBinding().getValue();
            }

            result[i] = value;
        }

        return result;
    }
}