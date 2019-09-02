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
