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

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Convenience Builder class to easily created Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class BindingBuilder {

    private final Bindings bindings = Bindings.create();

    private BindingBuilder() {
        super();
    }

    /**
     * Creates a new Builder.
     *
     * @return new Builder.
     */
    public static BindingBuilder create() {
        return new BindingBuilder();
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, Class<T> type) {
        bindings.bind(name, type);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, TypeReference<T> type) {
        bindings.bind(name, type);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, Class<T> type, T initialValue) {
        bindings.bind(name, TypeReference.with(type), initialValue);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, TypeReference<T> type, T initialValue) {
        bindings.bind(name, type, initialValue);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck) {
        bindings.bind(name, TypeReference.with(type), initialValue, validationCheck);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, TypeReference<T> type, T initialValue, Predicate<T> validationCheck) {
        bindings.bind(name, type, initialValue, validationCheck);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param mutable determines whether the value is mutable.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, Class<T> type, T initialValue, Predicate<T> validationCheck,
                                   boolean mutable) {
        bindings.bind(name, TypeReference.with(type), initialValue, validationCheck, mutable);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and an initial value.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param validationCheck validation to be performed when the value is changed.
     * @param mutable determines whether the value is mutable.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    public <T> BindingBuilder bind(String name, TypeReference<T> type, T initialValue, Predicate<T> validationCheck,
                                   boolean mutable) {
        bindings.bind(name, type, initialValue, validationCheck, mutable);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and the value will be retrieved using the supplied Supplier.
     *
     * @param name name of the Binding.
     * @param valueSupplier the value of the Binding will be retrieved using this Supplier.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     */
    public <T> BindingBuilder bind(String name, Supplier<T> valueSupplier, Class<T> type) {
        bindings.bind(name, valueSupplier, type);
        return this;
    }

    /**
     * Declares a new Binding given a name, type and the value will be retrieved using the supplied Supplier.
     *
     * @param name name of the Binding.
     * @param valueSupplier the value of the Binding will be retrieved using this Supplier.
     * @param type type reference of the Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     */
    public <T> BindingBuilder bind(String name, Supplier<T> valueSupplier, TypeReference<T> type) {
        bindings.bind(name, valueSupplier, type);
        return this;
    }

    /**
     * Binds the given Binding into this set of Bindings. Follows the same rules as adding a new Binding with name, type, etc.
     *
     * @param binding existing Binding.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     */
    public <T> BindingBuilder bind(Binding<T> binding) {
        bindings.bind(binding);
        return this;
    }

    /**
     * Binds all the given Bindings into this set of Bindings. Follows the same rules as adding a new Binding with name, type, etc.
     * The execution will stop if a Binding already exists.
     *
     *
     * @param binds existing Bindings.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if a Binding already exists.
     */
    public <T> BindingBuilder bind(Binding<T>...binds) {
        bind(Arrays.asList(binds));
        return this;
    }
    /**
     * Binds all the given Bindings into this set of Bindings. Follows the same rules as adding a new Binding with name, type, etc.
     * The execution will stop if a Binding already exists.
     *
     *
     * @param binds Bindings.
     * @param <T> generic type of the Binding.
     * @return this Builder.
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if a Binding already exists.
     */
    public <T> BindingBuilder bind(Collection<Binding<T>> binds) {
        bindings.bind(binds);
        return this;
    }

    /**
     * Builds a set of Bindings.
     *
     * @return creates a new set Bindings (with all the added values).
     */
    public Bindings build() {
        return bindings;
    }
}
