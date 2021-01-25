/*
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 1999-2021, Algorithmx Inc.
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

package org.algorithmx.rulii.bind.match;

import org.algorithmx.rulii.bind.Binding;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.TypeReference;

import java.util.Collections;
import java.util.Map;

/**
 * BindingMatchingStrategy that matches Bindings by the given Type.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class MatchByTypeMatchingStrategy implements BindingMatchingStrategy {

    public MatchByTypeMatchingStrategy() {
        super();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, Binding<T>> match(Bindings bindings, String name, TypeReference<T> type) {
        Assert.notNull(bindings, "bindings cannot be bull");
        Assert.notNull(type, "type cannot be bull");
        return Collections.unmodifiableMap(bindings.getBindings(type));
    }
}
