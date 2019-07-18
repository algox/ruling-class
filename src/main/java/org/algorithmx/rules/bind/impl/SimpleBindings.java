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

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, Class<T> type) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, null, null, true);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, TypeReference<T> type) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, null, null, true);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, Class<T> type, T initialValue) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, initialValue, null, true);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck)
            throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, type, initialValue, validationCheck, true);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck,
                               boolean mutable) throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, TypeReference.with(type), initialValue, validationCheck, mutable);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param typeRef type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, TypeReference<T> typeRef, T initialValue)
            throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, typeRef, initialValue, null, true);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param typeRef type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
    @Override
    public <T> Binding<T> bind(String name, TypeReference<T> typeRef, T initialValue, Predicate<T> validationCheck)
            throws BindingAlreadyExistsException, InvalidBindingException {
        return bind(name, typeRef, initialValue, validationCheck, true);
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param typeRef type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param mutable determines whether the value is mutable.
     * @param <T> generic type of the Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see SimpleBinding
     */
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

    /**
     * Retrieves the number of Bindings.
     *
     * @return number of Bindings.
     */
    @Override
    public int size() {
        return bindings.size();
    }

    /**
     * Clears all the Bindings.
     */
    @Override
    public void clear() {
        bindings.clear();
    }

    /**
     * Determines if a Binding with given name exists.
     *
     * @param name name of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    @Override
    public boolean contains(String name) {
        return bindings.containsKey(name);
    }

    /**
     * Determines if the Binding with given name and types exists.
     *
     * @param name name of the Binding.
     * @param typeRef generic ype of the Binding.
     * @param <T> generic type of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    @Override
    public <T> boolean contains(String name, TypeReference<T> typeRef) {
        Binding<?> result = bindings.get(name);
        return result != null
                ? result.isTypeAcceptable(typeRef.getType())
                    ? true : false
                : false;
    }

    /**
     * Creates an alias for an existing Binding.
     *
     * @param existingBindingName name of the existing Binding.
     * @param alias alias for the existing Binding.
     * @param <T> generic type of the Binding.
     * @return existing Binding.
     */
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

    /**
     * Retrieves the Binding identified by the given name.
     *
     * @param name name of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> Binding<T> getBinding(String name) {
        return (Binding<T>) bindings.get(name);
    }

    /**
     * Retrieves the value of the Binding with the given name.
     *
     * @param name name of the Binding.
     * @param <T> desired Type.
     * @return value if Binding is found.
     * @throws NoSuchBindingException if Binding is not found.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        return result.getValue();
    }

    /**
     * Sets the value of Binding with the given name.
     *
     * @param name name of the Binding.
     * @param value desired new value.
     * @param <T> generic type of the Binding.
     * @throws NoSuchBindingException if Binding is not found.
     */
    @Override
    public <T> void set(String name, T value) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        result.setValue(value);
    }

    /**
     * Retrieves the Binding identified by the given name.
     *
     * @param name name of the Binding.
     * @param typeRef type of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> typeRef) {
        Binding<T> result = getBinding(name);

        return result == null
                ? null
                : result.isAssignable(typeRef.getType())
                    ? result
                    : null;
    }

    /**
     * Retrieves all the Bindings of the given type.
     *
     * @param typeRef desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
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

    /**
     * Retrieves the Bindings as an Unmodifiable Map.
     *
     * @return unmodifiable Map of the Bindings.
     */
    @Override
    public Map<String, Binding<?>> asMap() {
       return Collections.unmodifiableMap(bindings);
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
