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
import org.algorithmx.rules.lib.apache.reflect.TypeUtils;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.TypeReference;
import org.algorithmx.rules.util.reflect.ObjectFactory;
import org.algorithmx.rules.util.reflect.ReflectionUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Default Parameter Resolver implementation.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultParameterResolver implements ParameterResolver {

    private boolean autoConvert = true;

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

            // Find all the matching bindings
            Map<String, Binding<Object>> matches = matcher.match(bindings, parameterDefinition.getName(),
                    TypeReference.with(parameterDefinition.getUnderlyingType()));
            int matchesCount = matches.size();

            if (matchesCount == 0) {
                result[index] = new ParameterMatch(parameterDefinition, null);
            } else if (matchesCount == 1) {
                Binding<Object> binding = matches.values().stream().findFirst().get();
                result[index] = new ParameterMatch(parameterDefinition, binding);
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
                    result[index] = new ParameterMatch(parameterDefinition, primaryBinding);
                } else {
                    // Too many matches found; cannot proceed.
                    throw new BindingException("Multiple Bindings found using ("
                            + matchingStrategy.getClass().getSimpleName() + "). Perhaps specify a primary Binding? ",
                            definition, parameterDefinition, matchingStrategy, matches.values());
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
            // Make sure matches are passed
            if (matches[i] == null) throw new UnrulyException("Invalid state. You cannot have a null match");
            // Strict checks that the binding exists; non strict lets the consumer deal with the consequences.
            result[i] = getValue(matches[i], definition, matchingStrategy, registry);
        }

        return result;
    }

    protected Object getValue(ParameterMatch match, MethodDefinition definition,
                              BindingMatchingStrategy matchingStrategy, ConverterRegistry registry) {
        Assert.notNull(match, "match cannot be null.");

        if (match.getDefinition().isBindingType()) {
            return match.getBinding();
        }

        Object result = match.getBinding() == null
                    ? getDefaultValue(match, definition, matchingStrategy, registry)
                    : match.getBinding().getValue();

        result = match.isOptional() && !(result instanceof Optional) ? Optional.of(result) : result;

        if (result != null && isAutoConvert()) {
            result = autoConvert(result, match, registry);
        }

        return result;
    }

    protected Object autoConvert(Object result, ParameterMatch match, ConverterRegistry registry) {
        if (match.getBinding() == null) return result;

        if (!TypeUtils.isAssignable(match.getDefinition().getType(), match.getBinding().getType())) {
            Converter converter = registry.find(match.getBinding().getType(), match.getDefinition().getType());
            if (converter != null) result = converter.convert(result, match.getDefinition().getType());
        }

        return result;
    }

    protected Object getDefaultValue(ParameterMatch match, MethodDefinition definition,
                                     BindingMatchingStrategy matchingStrategy, ConverterRegistry registry) {
        Object result = null;

        // There was no match; let's see if there is default value
        if (match.getBinding() == null) {
            result = ReflectionUtils.getDefaultValue(match.getDefinition().getType());

            if (match.getDefinition().getDefaultValueText() != null) {
                result = getValueFromDefaultText(match, definition, matchingStrategy, registry);
            }
        }

        return result;
    }

    protected Object getValueFromDefaultText(ParameterMatch match, MethodDefinition definition,
                                             BindingMatchingStrategy matchingStrategy, ConverterRegistry registry) {
        Object result = null;
        if (match.getDefinition().getDefaultValueText() != null) {
            Converter<String, ?> converter = registry.find(String.class, match.getDefinition().getType());

            if (converter == null) {
                throw new BindingException("Cannot find a converter that will convert default value ["
                        + match.getDefinition().getDefaultValueText() + "] to type ["
                        + match.getDefinition().getType() + "]", definition, match.getDefinition(),
                        matchingStrategy, null);
            }

            result = match.getDefinition().getDefaultValue(converter);
        }

        return result;
    }

    public boolean isAutoConvert() {
        return autoConvert;
    }

    public void setAutoConvert(boolean autoConvert) {
        this.autoConvert = autoConvert;
    }
}
