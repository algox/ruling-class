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
import org.algorithmx.rules.bind.BindingAlreadyExistsException;
import org.algorithmx.rules.bind.Bindings;
import org.algorithmx.rules.bind.InvalidBindingException;
import org.algorithmx.rules.bind.NoSuchBindingException;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Default implementation of the Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class SimpleBindings implements Bindings {

    // Stores all the Bindings
    private final Map<String, Binding<?>> bindings = createBindings();
    private final boolean selfAware;

    /**
     * Default Ctor. Self Reference added.
     */
    public SimpleBindings() {
        this(true);
    }

    /**
     * Creates Bindings.
     *
     * @param selfAware if true self reference binding is added.
     */
    public SimpleBindings(boolean selfAware) {
        super();
        this.selfAware = selfAware;
        // Create a self bind
        init(selfAware);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Bindings bind(String name, TypeReference<T> typeRef, T initialValue, Predicate<T> validationCheck, boolean mutable)
            throws BindingAlreadyExistsException, InvalidBindingException {
        DefaultBinding<T> result = new DefaultBinding(name, typeRef.getType(), initialValue, validationCheck);
        result.setMutable(mutable);
        bind(result);
        return this;
    }

    @Override
    public <T> Bindings bind(String name, Supplier<T> valueSupplier, TypeReference<T> typeRef) throws BindingAlreadyExistsException {
        DefaultBinding<T> result = new DefaultBinding(name, typeRef.getType(), valueSupplier);
        bind(result);
        return this;
    }

    @Override
    public <T> Bindings bind(Binding<T> binding) {
        Assert.notNull(binding, "binding cannot be null");
        // Try and put the Binding
        Binding<?> existingBinding = bindings.putIfAbsent(binding.getName(), binding);
        // Looks like we already have a binding
        if (existingBinding != null) throw new BindingAlreadyExistsException(binding.getName());
        return this;
    }

    @Override
    public <T> Bindings bind(Collection<Binding<T>> existingBindings) {
        for (Binding<T> binding : existingBindings) {
            if (binding == null) continue;
            bind(binding);
        }
        return this;
    }

    @Override
    public int size() {
        return bindings.size();
    }

    @Override
    public void clear() {
        bindings.clear();
        init(selfAware);
    }

    @Override
    public Iterator<Binding<?>> iterator() {
        return bindings.values().iterator();
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Binding<T> getBinding(String name) {
        return (Binding<T>) bindings.get(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        return result.getValue();
    }

    @Override
    public <T> void set(String name, T value) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        result.setValue(value);
    }

    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> typeRef) {
        Binding<T> result = getBinding(name);

        return result == null
                ? null
                : result.isAssignable(typeRef.getType())
                    ? result
                    : null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Set<Binding<T>> getBindings(TypeReference<T> typeRef) {
        Set<Binding<T>> result = new HashSet<>();

        for (Binding<?> binding : bindings.values()) {
            if (binding.isAssignable(typeRef.getType())) {
                result.add((Binding<T>) binding);
            }
        }

        return Collections.unmodifiableSet(result);
    }

    @Override
    public Map<String, ?> asMap() {
        Map<String, Object> result = new HashMap<>();

        for (Map.Entry<String, Binding<?>> entry : bindings.entrySet()) {
            result.put(entry.getKey(), entry.getValue().getValue());
        }

        return Collections.unmodifiableMap(result);
    }

    /**
     * Initialize the Bindings with a self reference.
     *
     * @param selfAware if a Binding to itself must be created.
     */
    protected void init(boolean selfAware) {
        if (selfAware) bind(Bindings.SELF_BIND_NAME, Bindings.class, this, null, false);
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
    public String toString() {
        StringBuilder result = new StringBuilder("Bindings {" + System.lineSeparator());

        for (Binding<?> binding : bindings.values()) {
            if (binding.get() instanceof Bindings) continue;
            result.append(binding.toString() + System.lineSeparator());
        }

        result.append("}");
        return result.toString();
    }
}
