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

import org.algorithmx.rules.bind.*;

import java.lang.reflect.Type;
import java.util.*;
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

    public SimpleBindings() {
        super();
    }

    @Override
    public <T> Binding<T> bind(String name, Class<T> type) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, null, null, true);
    }

    @Override
    public <T> Binding<T> bind(String name, TypeReference<T> type) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, null, null, true);
    }

    @Override
    public <T> Binding<T> bind(String name, Class<T> type, T initialValue) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, initialValue, null, true);
    }

    @Override
    public <T> Binding<T> bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck)
            throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, initialValue, validationCheck, true);
    }

    @Override
    public <T> Binding<T> bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck,
                               boolean mutable) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, TypeReference.with(type), initialValue, validationCheck, mutable);
    }

    @Override
    public <T> Binding<T> bind(String name, TypeReference<T> typeRef, T initialValue)
            throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, typeRef, initialValue, null, true);
    }

    @Override
    public <T> Binding<T> bind(String name, TypeReference<T> typeRef, T initialValue, Predicate<T> validationCheck)
            throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, typeRef, initialValue, validationCheck, true);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Binding<T> bind(String name, TypeReference<T> typeRef, T initialValue, Predicate<T> validationCheck, boolean mutable)
            throws BindingAlreadyExistsException, InvalidBindingException {
        SimpleBinding<T> result = new SimpleBinding<T>(name, typeRef.getType(), initialValue, validationCheck);
        result.setMutable(mutable);

        // Try and put the Binding
        Binding<?> existingBinding = bindings.putIfAbsent(name, result);
        // Looks like we already have a binding
        if (existingBinding != null) throw new BindingAlreadyExistsException(name);

        return result;
    }

    @Override
    public <T> Binding<T> bind(String name, Supplier<T> valueSupplier, Class<T> type) throws BindingAlreadyExistsException {
        return bind(name, valueSupplier, TypeReference.with(type));
    }

    @Override
    public <T> Binding<T> bind(String name, Supplier<T> valueSupplier, TypeReference<T> typeRef) throws BindingAlreadyExistsException {
        SimpleBinding<T> result = new SimpleBinding<T>(name, typeRef.getType(), valueSupplier);
        // Try and put the Binding
        Binding<?> existingBinding = bindings.putIfAbsent(name, result);
        // Looks like we already have a binding
        if (existingBinding != null) throw new BindingAlreadyExistsException(name);
        return result;
    }

    @Override
    public int size() {
        return bindings.size();
    }

    @Override
    public void clear() {
        bindings.clear();
    }

    @Override
    public boolean contains(String name) {
        return bindings.containsKey(name);
    }

    @Override
    public <T> boolean contains(String name, TypeReference<T> typeRef) {
        Binding<?> result = bindings.get(name);
        return result != null
                ? result.isTypeAcceptable(typeRef.getType())
                    ? true : false
                : false;
    }

    @Override
    public <T> Binding<T> alias(String existingBindingName, String alias) {
        Binding<T> result = getBinding(existingBindingName);

        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(existingBindingName);
        // Make sure the alias doesn't exist
        if (contains(alias)) throw new BindingAlreadyExistsException(alias);

        this.bindings.put(alias, result);

        return result;
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
     * Creates the data structure to store all the Bindings.
     *
     * @return creates the data structure that will ultimately store the Bindings. Defaulted to HashMap.
     */
    protected Map<String, Binding<?>> createBindings() {
        return new HashMap<>();
    }
}
