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

import org.algorithmx.rules.util.TypeReference;

import java.util.Map;

/**
 * Bindings with Scopes. Allows the user to defaultObjectFactory/remove scopes around the Bindings. Each Binding is tied to a
 * Scope and Binding is removed once the Scope is removed. Binding is treated much like a Local variable on a method stack.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface ScopedBindings extends Bindings {

    /**
     * Creates the default implementation of these Bindings.
     *
     * @param bindings initial set of bindings.
     * @return new default implementation instance.
     */
    static ScopedBindings create(Bindings bindings) {
        return new DefaultScopedBindings(bindings);
    }

    /**
     * Creates the default implementation of these Bindings.
     * @return new default implementation instance.
     */
    static ScopedBindings create() {
        return new DefaultScopedBindings();
    }

    /**
     * Returns the current working scope.
     *
     * @return working scope.
     */
    Bindings getCurrentScope();

    /**
     * Returns the root scope.
     *
     * @return root scope.
     */
    Bindings getRootScope();

    /**
     * Creates new scope and pushes it to the top of Stack.
     *
     * @return the newly created Bindings.
     */
    Bindings addScope();

    /**
     * Pops the working Bindings off the Stack.
     *
     * @return the removed Bindings.
     */
    Bindings removeScope();

    /**
     * Pops the working Bindings off the Stack till we match our desired target.
     *
     * @param target removes all scopes including target and above.
     * @return the removed Bindings.
     */
    Bindings removeScope(Bindings target);

    /**
     * Binds the given Binding into the current scope. Follows the same rules as adding a new Binding with name, type, etc.
     *
     * @param binding existing Binding.
     * @param <S> type of Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws BindingAlreadyExistsException thrown if the Binding already exists.
     */
    @Override
    default <S extends Bindings, T> S bind(Binding<T> binding) {
        getCurrentScope().bind(binding);
        return (S) this;
    }

    /**
     * Retrieves the Binding identified by the given name. The search starts with working scope and goes back the Stack
     * until the initial scope. The search stops once a match is found.
     *
     * @param name name of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    @Override
    <T> Binding<T> getBinding(String name);

    /**
     * Retrieves the Binding identified by the given name and type. The search starts with working scope and goes back the Stack
     * until the initial scope. The search stops once a match is found.
     *
     * @param name name of the Binding.
     * @param type type of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    @Override
    <T> Binding<T> getBinding(String name, TypeReference<T> type);

    /**
     * Retrieves all the Bindings of the given type. The search starts with working scope and goes back the Stack
     * until a match is found.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
    @Override
    <T> Map<String, Binding<T>> getBindings(TypeReference<T> type);

    /**
     * Retrieves all the Bindings of the given type. The search starts with working scope and goes back the Stack
     * until the initial scope.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */

    <T> Map<String, Binding<T>> getAllBindings(TypeReference<T> type);

    /**
     * Retrieves the Binding values as an Unmodifiable Map. The retrieval starts with working scope and goes back the Stack
     * until the initial scope.
     *
     * @return unmodifiable Map of the Binding values.
     */
    @Override
    Map<String, ?> asMap();

    /**
     * Retrieves the number of Bindings in all the scopes. All Bindings are accounted for (does not account for unique names).
     * Use uniqueSize() for unique count.
     *
     * @return total number of Bindings (in all Scopes).
     */
    @Override
    int size();

    /**
     * Retrieves the number of unique (by name) Bindings in all the scopes.
     *
     * @return unique Bindings count.
     */
    default int uniqueSize() {
        return asMap().size();
    }

    /**
     * Returns back a immutable version of this Bindings.
     *
     * @return immutable version of this.
     */
    @Override
    default ScopedBindings asImmutableBindings() {
        return new ImmutableScopedBindings(this);
    }
}
