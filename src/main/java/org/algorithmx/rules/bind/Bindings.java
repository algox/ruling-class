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

import org.algorithmx.rules.bind.loader.BindingLoader;
import org.algorithmx.rules.bind.loader.FieldBindingLoader;
import org.algorithmx.rules.bind.loader.MapBindingLoader;
import org.algorithmx.rules.bind.loader.PropertyBindingLoader;
import org.algorithmx.rules.spring.util.Assert;

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
     * Creates the default implementation of ScopedBindings.
     * @return new default scoped implementation instance.
     */
    static ScopedBindings createWithScopes() {
        return new DefaultScopedBindings();
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
        return bind(BindingBuilder.with(name).type(type).build());
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
        Bindings result = new DefaultBindings();
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
     * Binds this Binding to itself. This is useful when Rules want to use the Bindings (to create new ones etc)
     *
     * @param name name to use for the Binding name (eg : "Bindings")
     * @param <S> type of Bindings.
     * @return this Bindings (fluent interface).
     */
    default <S extends Bindings> S bindSelf(String name) {
        bind(BindingBuilder.with(name).value(this).primary(false).editable(false).build());
        return (S) this;
    }

    /**
     * Binds this Binding to itself. This is useful when Rules want to use the Bindings (to create new ones etc)
     *
     * @param name name to use for the Binding name (eg : "Bindings")
     * @param <S> type of Bindings.
     * @return this Bindings (fluent interface).
     */
    default <S extends Bindings> S bindImmutableSelf(String name) {
        bind(BindingBuilder.with(name).value(asImmutableBindings()).primary(false).editable(false).build());
        return (S) this;
    }

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
     * Binds each readable property on the given Bean. This is just convenience method if you want to control which
     * properties get added use bindUsing.
     *
     * @param bean parent bean.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Value object.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    default <S extends Bindings, T> S bindProperties(T bean) {
        return bindUsing(new PropertyBindingLoader<>(), bean);
    }

    /**
     * Binds each declared field on the given Bean. This is just convenience method if you want to control which
     * fields get added use bindUsing.
     *
     * @param bean parent bean.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Value object.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    default <S extends Bindings, T> S bindFields(T bean) {
        return bindUsing(new FieldBindingLoader<>(), bean);
    }

    /**
     * Binds each key on the given Map. This is just convenience method if you want to control which
     * keys get added use bindUsing(BindingLoader loader, T value)
     *
     * @param map key/values.
     * @param <S> type of Bindings.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    default <S extends Bindings> S bindMap(Map<String, ?> map) {
        return bindUsing(new MapBindingLoader(), map);
    }

    /**
     * Retrieves the number of Bindings.
     *
     * @return number of Bindings.
     */
    int size();

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
     * Retrieves the value of the Binding with the given name.
     *
     * @param name name of the Binding.
     * @param <T> desired Type.
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

}
