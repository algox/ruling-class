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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.bind.impl.SimpleBindings;

import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * The interface that is used to store and find Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Bindings {

    /**
     * Creates an instance of the Bindings.
     *
     * @return new instance of the Bindings.
     */
     static Bindings create() {
        return new SimpleBindings();
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, Class<T> type) throws BindingAlreadyExistsException, InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, TypeReference<T> type) throws BindingAlreadyExistsException, InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, Class<T> type, T initialValue) throws BindingAlreadyExistsException,
            InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, TypeReference<T> type, T initialValue) throws BindingAlreadyExistsException,
            InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck)
            throws BindingAlreadyExistsException, InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, TypeReference<T> type, T initialValue, Predicate<T> validationCheck)
            throws BindingAlreadyExistsException, InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param mutable determines whether the value is mutable.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck, boolean mutable)
            throws BindingAlreadyExistsException, InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param mutable determines whether the value is mutable.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    <T> Binding<T> bind(String name, TypeReference<T> type, T initialValue, Predicate<T> validationCheck, boolean mutable)
            throws BindingAlreadyExistsException, InvalidBindingException;

    /**
     * Declares a new Binding given a name, type and the value will be retrieved using the supplied Supplier.
     *
     * @param name name of the Binding.
     * @param valueSupplier the value of the Binding will be retrieved using this Supplier.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     */
    <T> Binding<T> bind(String name, Supplier<T> valueSupplier, Class<T> type) throws BindingAlreadyExistsException;

    /**
     * Declares a new Binding given a name, type and the value will be retrieved using the supplied Supplier.
     *
     * @param name name of the Binding.
     * @param valueSupplier the value of the Binding will be retrieved using this Supplier.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return the resulting Binding.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     */
    <T> Binding<T> bind(String name, Supplier<T> valueSupplier, TypeReference<T> type) throws BindingAlreadyExistsException;

    /**
     * Retrieves the number of Bindings.
     *
     * @return number of Bindings.
     */
    int size();

    /**
     * Clears all the Bindings.
     */
    void clear();

    /**
     * Determines if a Binding with given name exists.
     *
     * @param name name of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    boolean contains(String name);

    /**
     * Determines if the Binding with given name and types exists.
     *
     * @param name name of the Binding.
     * @param type class type of the Binding.
     * @param <T> generic type of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    default <T> boolean contains(String name, Class<T> type) {
        return contains(name, TypeReference.with(type));
    }

    /**
     * Determines if the Binding with given name and types exists.
     *
     * @param name name of the Binding.
     * @param type generic type of the Binding.
     * @param <T> generic type of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    <T> boolean contains(String name, TypeReference<T> type);

    /**
     * Creates an alias for an existing Binding.
     *
     * @param existingBindingName name of the existing Binding.
     * @param alias alias for the existing Binding.
     * @param <T> generic type of the Binding.
     * @return existing Binding.
     */
    <T> Binding<T> alias(String existingBindingName, String alias);

    /**
     * Retrieves the Binding identified by the given name.
     *
     * @param name name of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    <T> Binding<T> getBinding(String name);

    /**
     * Retrieves the value of the Binding with the given name.
     *
     * @param name name of the Binding.
     * @param <T> desired Type.
     * @param <T> generic type of the Binding.
     * @return value if Binding is found.
     * @throws NoSuchBindingException if Binding is not found.
     */
    <T> T get(String name);

    /**
     * Sets the value of Binding with the given name.
     *
     * @param name name of the Binding.
     * @param value desired new value.
     * @param <T> generic type of the Binding.
     * @throws NoSuchBindingException if Binding is not found.
     */
    <T> void set(String name, T value);

    /**
     * Retrieves the Binding identified by the given name.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    default <T> Binding<T> getBinding(String name, Class<T> type) {
        return getBinding(name, TypeReference.with(type));
    }

    /**
     * Retrieves the Binding identified by the given name.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    <T> Binding<T> getBinding(String name, TypeReference<T> type);

    /**
     * Retrieves all the Bindings of the given type.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
    default <T> Set<Binding<T>> getBindings(Class<T> type) {
        return getBindings(TypeReference.with(type));
    }

    /**
     * Retrieves all the Bindings of the given type.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
    <T> Set<Binding<T>> getBindings(TypeReference<T> type);

    /**
     * Retrieves the Binding values as an Unmodifiable Map.
     *
     * @return unmodifiable Map of the Binding values.
     */
    Map<String, ?> asMap();

}
