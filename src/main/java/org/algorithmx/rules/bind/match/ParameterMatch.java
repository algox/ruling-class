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
import org.algorithmx.rules.core.model.ParameterDefinition;
import org.algorithmx.rules.lib.spring.util.Assert;

/**
 * Stores the parameter with it's corresponding matched binding(if one is found) for a given match.
 *
 * @author Max Arulananthan.
 * @since 1.0
 *
*/
public class ParameterMatch {

    private final ParameterDefinition definition;
    private final Binding<Object> binding;
    private final boolean isBinding;

    public ParameterMatch(ParameterDefinition definition, Binding<Object> binding, boolean isBinding) {
        super();
        Assert.notNull(definition, "definition cannot be null.");
        this.definition = definition;
        this.binding = binding;
        this.isBinding = isBinding;
    }

    public ParameterDefinition getDefinition() {
        return definition;
    }

    public Binding<Object> getBinding() {
        return binding;
    }

    public boolean isBinding() {
        return isBinding;
    }

    @Override
    public String toString() {
        return "ParameterMatch{" +
                "definition=" + definition +
                ", binding=" + binding +
                ", isBinding=" + isBinding +
                '}';
    }
}
