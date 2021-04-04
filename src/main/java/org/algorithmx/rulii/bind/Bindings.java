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

import org.algorithmx.rulii.bind.load.BindingLoader;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.TypeReference;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

/**
 * The interface that is used to store and find Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface Bindings extends Iterable<Binding<?>> {

    /**
     * Creates the default implementation of these Bindings.
     * @return new default implementation instance.
     */
    static Bindings create() {
        return new DefaultBindings();
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, T initialValue) {
        return bind(BindingBuilder.with(name).value(initialValue).build());
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, Class<T> type) {
        bind(BindingBuilder.with(name).type(type).build());
        return (S) this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, TypeReference<T> type) {
        return bind(BindingBuilder.with(name).type(type).build());
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, Class<T> type, T initialValue) {
        return bind(BindingBuilder.with(name).type(type).value(initialValue).build());
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, TypeReference<T> type, T initialValue) {
        return bind(BindingBuilder.with(name).type(type).value(initialValue).build());
    }

    /**
     * Creates a new Binding using a BindingDeclaration. The type of the Binding will be the type of the value.
     * In case the value is null then the type is Object.class. Note that generics are not available and hence the
     * type that is declared will NOT have any generic type.
     *
     * @param declaration declaration details.
     * @param <S> type of Bindings.
     * @return this Bindings (fluent interface).
     */
    default <S extends Bindings> S bind(BindingDeclaration declaration) {
        bind(BindingBuilder.with(declaration).build());
        return (S) this;
    }

    /**
     * Creates Bindings and adds them all.
     *
     * @param declarations binding declarations.
     * @param <S> type of Bindings.
     * @return this Bindings (fluent interface).
     */
    default <S extends Bindings> S bind(BindingDeclaration...declarations)  {
        Assert.notNull(declarations, "declarations cannot be null");
        Bindings result = create();
        Arrays.stream(declarations).forEach(result::bind);
        return (S) result;
    }

    /**
     * Binds the given Binding into this set of Bindings. Follows the same rules as adding a new Binding with name, type, etc.
     *
     * @param binding existing Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     */
    <S extends Bindings, T> S bind(Binding<T> binding) throws BindingAlreadyExistsException;

    /**
     * Binding Loader an load a collection of Bindings from the given value object.
     *
     * @param loader BindingLoader to use (see PropertyBindingLoader, FieldBindingLoader, MapBindingLoader)
     * @param value value object (Bean, Map, etc)
     * @param <S> type of Bindings.
     * @param <T> generic type of the Value object.
     * @return this Bindings (fluent interface).
     */
    default <S extends Bindings, T> S bindUsing(BindingLoader<T> loader, T value) {
        Assert.notNull(loader, "loader cannot be null.");
        Assert.notNull(value, "value cannot be null.");
        loader.load(this, value);
        return (S) this;
    }

    /**
     * Retrieves the number of Bindings.
     *
     * @return number of Bindings.
     */
    int size();

    /**
     * Determines whether there are any Bindings.
     *
     * @return true if its empty; false otherwise.
     */
    default boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Retrieves the Binding identified by the given name.
     *
     * @param name name of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    <T> Binding<T> getBinding(String name);

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
     * Retrieves the Binding identified by the given name and type.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    <T> Binding<T> getBinding(String name, TypeReference<T> type);

    /**
     * Determines if a Binding with given name exists.
     *
     * @param name name of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    default boolean contains(String name) {
        return getBinding(name) != null;
    }

    /**
     * Determines if the Binding with given name and types exists.
     *
     * @param name name of the Binding.
     * @param type class type of the Binding.
     * @param <T> generic type of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    default <T> boolean contains(String name, Class<T> type) {
        return getBinding(name, type) != null;
    }

    /**
     * Determines if the Binding with given name and types exists.
     *
     * @param name name of the Binding.
     * @param type generic type of the Binding.
     * @param <T> generic type of the Binding.
     * @return true if Binding exists; false otherwise.
     */
    default <T> boolean contains(String name, TypeReference<T> type) {
        Binding<?> result = getBinding(name);
        return result != null
                ? result.getType().equals(type.getType()) ? true : false
                : false;
    }

    /**
     * Retrieves all the Bindings of the given type.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
    default <T> Map<String, Binding<T>> getBindings(Class<T> type) {
        return getBindings(TypeReference.with(type));
    }

    /**
     * Retrieves all the Bindings of the given type.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
    <T> Map<String, Binding<T>> getBindings(TypeReference<T> type);

    /**
     * Retrieves the value of the Binding with the given name.
     *
     * @param name name of the Binding.
     * @param <T> generic type of the Binding.
     * @return value if Binding is found.
     * @throws NoSuchBindingException if Binding is not found.
     */
    default <T> T getValue(String name) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        return result.getValue();
    }

    /**
     * Retrieves the value of the Binding with the given name.
     *
     * @param name name of the Binding.
     * @param type required type.
     * @param <T> generic type of the Binding.
     * @return value if Binding is found.
     * @throws NoSuchBindingException if Binding is not found.
     */
    default <T> T getValue(String name, Class<T> type) {
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
    default <T> void setValue(String name, T value) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        result.setValue(value);
    }

    /**
     * Retrieves all the Binding Names (ie. keys).
     *
     * @return all the used keys.
     */
    Set<String> getNames();

    /**
     * Retrieves the Binding values as an Unmodifiable Map.
     *
     * @return unmodifiable Map of the Binding values.
     */
    Map<String, ?> asMap();

    /**
     * Returns back a immutable version of this Bindings.
     *
     * @return immutable version of this.
     */
    default Bindings asImmutableBindings() {
        return new ImmutableBindings(this);
    }

    /**
     * Beautified version of the Bindings.
     *
     * @param prefix for spacing.
     * @return textual version of the Bindings.
     */
    String prettyPrint(String prefix);
}
