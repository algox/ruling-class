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
package org.algorithmx.rules.bind.impl;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.BindingMatchingStrategy;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Strategy class that matches Bindings by the given name.
 *
 * @author Max Arulananthan
 * @Since 1.0
 */
public class MatchByNameBindingMatchingStrategy implements BindingMatchingStrategy {

    public MatchByNameBindingMatchingStrategy() {
        super();
    }

    /**
     * Returns a set of Bindings that match the given name.
     *
     * @param bindings bindings.
     * @param name desired name.
     * @param type desired type.
     * @return all the matches; Will be an empty Set if no matches are found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<Binding<T>> match(Bindings bindings, String name, TypeReference<T> type) {
        Assert.notNull(bindings, "bindings cannot be bull");
        Assert.notNull(name, "name cannot be bull");

        Set<Binding<T>> result = new HashSet<>();
        // Look for the Binding by name
        Binding<T> binding = bindings.getBinding(name);
        // Add the Binding (if we found one)
        if (binding != null) result.add(binding);

        return Collections.unmodifiableSet(result);
    }
}
