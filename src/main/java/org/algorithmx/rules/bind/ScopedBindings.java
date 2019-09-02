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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface ScopedBindings extends Bindings {

    Bindings getCurrentScope();

    Iterable<Bindings> getScopes();

    Iterable<Bindings> getReverseScopes();

    Bindings newScope();

    Bindings endScope();

    @Override
    default <T> Binding<T> getBinding(String name) {
        Binding<T> result = null;

        for (Bindings scope : getScopes()) {
            result = scope.getBinding(name);
            if (result != null) break;
        }

        return result;
    }

    @Override
    default <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        Binding<T> result = null;

        for (Bindings scope : getScopes()) {
            result = scope.getBinding(name, type);
            if (result != null) break;
        }

        return result;
    }

    @Override
    default <T> Set<Binding<T>> getBindings(TypeReference<T> type) {
        Set<Binding<T>> result = new HashSet<>();

        for (Bindings scope : getReverseScopes()) {
            result.addAll(scope.getBindings(type));
        }

        return result;
    }

    @Override
    default Map<String, ?> asMap() {
        Map<String, Object> result = new HashMap<>();

        for (Iterator<Binding<?>> it = iterator(); it.hasNext();) {
            Binding<?> binding = it.next();
            result.put(binding.getName(), binding.getValue());
        }

        return result;
    }

    @Override
    default int size() {
        int result = 0;

        for (Bindings scope : getScopes()) {
            result += scope.size();
        }

        return result;
    }

    default Iterator<Binding<?>> iterator() {
        Set<Binding<?>> result = new HashSet<>();

        for (Bindings scope : getReverseScopes()) {
            for (Iterator<Binding<?>> it = scope.iterator(); it.hasNext();) {
                result.add(it.next());
            }
        }

        return result.iterator();
    }

    @Override
    default <T> Bindings bind(String name, TypeReference<T> type, T initialValue, Predicate<T> validationCheck, boolean mutable) throws BindingAlreadyExistsException, InvalidBindingException {
        return getCurrentScope().bind(name, type, initialValue, validationCheck, mutable);
    }

    @Override
    default <T> Bindings bind(Binding<T> binding) {
        return getCurrentScope().bind(binding);
    }

    @Override
    default <T> Bindings bind(Collection<Binding<T>> bindings) {
        return getCurrentScope().bind(bindings);
    }

    @Override
    default <T> Bindings bind(String name, Supplier<T> valueSupplier, TypeReference<T> type) throws BindingAlreadyExistsException {
        return getCurrentScope().bind(name, valueSupplier, type);
    }
}
