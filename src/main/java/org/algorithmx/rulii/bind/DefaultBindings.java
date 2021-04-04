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

package org.algorithmx.rulii.bind;

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.TypeReference;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default implementation of the Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultBindings implements Bindings {

    // Stores all the Bindings
    private final Map<String, Binding<?>> bindings = createBindings();
    private final Set<String> reservedWords = new HashSet<>();

    /**
     * Default Ctor. Self Reference added.
     */
    DefaultBindings() {
        super();
        this.reservedWords.addAll(ReservedBindings.reservedBindings());
    }

    @Override
    public <S extends Bindings, T> S bind(Binding<T> binding) {
        Assert.notNull(binding, "binding cannot be null");

        if (reservedWords.contains(binding.getName())) {
            throw new InvalidBindingException("Binding name [" + binding.getName() + "] is a reserved name. " +
                    "Please rename and try again. Given Binding [" + binding + "]");
        }

        return promiscuousBind(binding);
    }

    public <S extends Bindings, T> S promiscuousBind(Binding<T> binding) {
        Assert.notNull(binding, "binding cannot be null");

        // Try and put the Binding
        Binding existingBinding = bindings.putIfAbsent(binding.getName(), binding);

        // Looks like we already have a binding
        if (existingBinding != null) {
            throw new BindingAlreadyExistsException(existingBinding);
        }

        return (S) this;
    }

    @Override
    public int size() {
        return bindings.size();
    }

    @Override
    public Iterator<Binding<?>> iterator() {
        return bindings.values().iterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Binding<T> getBinding(String name) {
        Assert.notNull(name, "name cannot be null.");
        return (Binding<T>) bindings.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getValue(String name) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        return result.getValue();
    }

    @Override
    public <T> void setValue(String name, T value) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        result.setValue(value);
    }

    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> typeRef) {
        Binding<T> result = getBinding(name);
        // Make sure it also matches the Type
        return result != null && result.isTypeAcceptable(typeRef.getType())
                ? result
                : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Map<String, Binding<T>> getBindings(TypeReference<T> typeRef) {
        Map<String, Binding<T>> result = new HashMap<>();

        for (Binding<?> binding : bindings.values()) {
            if (binding.isTypeAcceptable(typeRef.getType())) {
                result.put(binding.getName(), (Binding<T>) binding);
            }
        }

        return Collections.unmodifiableMap(result);
    }

    @Override
    public Map<String, ?> asMap() {
        Map<String, Object> result = new HashMap<>();

        for (Binding<?> binding : this) {
            result.put(binding.getName(), binding.getValue());
        }

        return result;
    }

    @Override
    public Set<String> getNames() {
        return bindings.keySet();
    }

    /**
     * Creates the data structure to store all the Bindings.
     *
     * @return creates the data structure that will ultimately store the Bindings. Defaulted to HashMap.
     */
    protected Map<String, Binding<?>> createBindings() {
        return new ConcurrentHashMap<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!Bindings.class.isAssignableFrom(o.getClass())) return false;
        Bindings other = (Bindings) o;
        return asMap().equals(other.asMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(bindings);
    }

    public String prettyPrint(String prefix) {
        StringBuilder result = new StringBuilder(System.lineSeparator());

        for (Binding<?> binding : bindings.values()) {
            if (binding.getValue() instanceof Bindings) continue;

            result.append(prefix + binding.getSummary() + System.lineSeparator());
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return prettyPrint("");
    }
}
