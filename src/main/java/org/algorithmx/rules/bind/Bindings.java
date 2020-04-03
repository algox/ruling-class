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

import org.algorithmx.rules.bind.impl.DefaultBindings;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Arrays;
import java.util.Collection;
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
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param mutable determines whether the value can be changed.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, TypeReference type, T initialValue, boolean mutable)
            throws BindingAlreadyExistsException, InvalidBindingException {
        bind(BindingBuilder.with(name).type(type).value(initialValue).mutable(mutable).build());
        return (S) this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param mutable determines whether the value can be changed.
     * @param primary determines whether the Binding is a primary candidate.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    default <S extends Bindings, T> S bind(String name, TypeReference type, T initialValue, boolean mutable, boolean primary)
            throws BindingAlreadyExistsException, InvalidBindingException {
        bind(BindingBuilder.with(name).type(type).value(initialValue).mutable(mutable).primary(primary).build());
        return (S) this;
    }

    /**
     * Binds all the given Bindings into this set of Bindings. Follows the same rules as adding a new Binding with name, type, etc.
     * The execution will stop if a Binding already exists.
     *
     *
     * @param bindings existing Bindings.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    default <S extends Bindings, T> S bind(Binding<T>...bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        return (S) bind(Arrays.asList(bindings));
    }

    /**
     * Binds all the given Bindings into this set of Bindings. Follows the same rules as adding a new Binding with name, type, etc.
     * The execution will stop if a Binding already exists.
     *
     *
     * @param bindings existing Bindings.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    default <S extends Bindings, T> S bind(Collection<Binding<T>> bindings) {
        Assert.notNull(bindings, "bindings cannot be null.");
        for (Binding binding : bindings) {
            bind(binding);
        }
        return (S) this;
    }

    /**
     * Binds each readable property on the given Bean.
     *
     * @param bean parent bean.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    /*default Bindings bindProperties(Object bean) {
        return bindProperties(bean, (String name) -> name);
    }*/

    /**
     * Binds each readable property on the given Bean.
     *
     * @param bean parent bean.
     * @param nameGenerator generator that will determine the Binding name for each property.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    /*default Bindings bindProperties(Object bean, Function<String, String> nameGenerator) {
        Assert.notNull(bean, "bean cannot be null.");
        Assert.notNull(nameGenerator, "nameGenerator cannot be null.");

        try {
            ReflectionUtils.traverseProperties(bean.getClass(), property -> property.getReadMethod() != null,
                    property -> {
                        try {
                            // Get the value via the getter
                            Object value = property.getReadMethod().invoke(bean);
                            // Bind the property
                            bind(nameGenerator.apply(property.getName()), TypeReference.with(
                                    property.getReadMethod().getGenericReturnType()), value);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            // Couldn't get the value
                            throw new UnrulyException("Error trying to retrieve property [" + property.getName()
                                    + "] on Bean class [" + bean.getClass() + "]", e);
                        }
                    });
        } catch (IntrospectionException e) {
            throw new UnrulyException("Error trying to Introspect [" + bean.getClass() + "]", e);
        }

        return this;
    }*/

    /**
     * Binds each declared field on the given Bean.
     *
     * @param bean parent bean.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    /*default Bindings bindFields(Object bean) {
        return bindFields(bean, (String name) -> name);
    }*/

    /**
     * Binds each declared field on the given Bean.
     *
     * @param bean parent bean.
     * @param nameGenerator generator that will determine the Binding name for each field.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if a Binding already exists.
     */
    /*default Bindings bindFields(Object bean, Function<String, String> nameGenerator) {
        Assert.notNull(bean, "bean cannot be null.");
        Assert.notNull(nameGenerator, "nameGenerator cannot be null.");

        ReflectionUtils.traverseFields(bean.getClass(), null, field -> {
            try {
                Object value = field.get(bean);
                bind(nameGenerator.apply(field.getName()), TypeReference.with(field.getGenericType()), value);
            } catch (IllegalAccessException e) {
                // Couldn't get the value
                throw new UnrulyException("Error trying to retrieve field [" + field.getName()
                        + "] on Bean class [" + bean.getClass() + "]", e);
            }
        });

        return this;
    }*/


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
    default <T> boolean contains(String name, TypeReference<T> type) {
        Binding<?> result = getBinding(name);
        return result != null
                ? result.isTypeAcceptable(type.getType())
                ? true : false
                : false;
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
     * Retrieves the value of the Binding with the given name.
     *
     * @param name name of the Binding.
     * @param <T> desired Type.
     * @param <T> generic type of the Binding.
     * @return value if Binding is found.
     * @throws NoSuchBindingException if Binding is not found.
     */
    default <T> T get(String name) {
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
    default <T> void set(String name, T value) {
        Binding<T> result = getBinding(name);
        // Could not find Binding
        if (result == null) throw new NoSuchBindingException(name);
        result.setValue(value);
    }

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
