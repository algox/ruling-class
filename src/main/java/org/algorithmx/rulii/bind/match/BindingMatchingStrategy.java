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
import org.algorithmx.rulii.util.TypeReference;

import java.util.Map;

/**
 * Strategy class that matches Bindings to a desired criteria (such as name, type).
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface BindingMatchingStrategy {

    /**
     * Returns the default Strategy.
     *
     * @return default strategy.
     */
    static BindingMatchingStrategy create() {
        return BindingMatchingStrategyType.getDefault().getStrategy();
    }

    /**
     * Returns a set a Bindings that match a set criteria that is determined by the implementing class.
     *
     * @param bindings bindings.
     * @param name desired name.
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return Bindings that match the criteria.
     */
    default <T> Map<String, Binding<T>> match(Bindings bindings, String name, Class<T> type) {
        return match(bindings, name, TypeReference.with(type));
    }

    /**
     * Returns a set a Bindings that match a set criteria that is determined by the implementing class.
     *
     * @param bindings bindings.
     * @param name desired name.
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return Bindings that match the criteria.
     */
    <T> Map<String, Binding<T>> match(Bindings bindings, String name, TypeReference<T> type);
}
