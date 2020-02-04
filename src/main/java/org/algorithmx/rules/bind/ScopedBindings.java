/**
 * This software is licensed under the Apache 2 license, quoted below.
 *
 * Copyright (c) 2019, algorithmx.org (dev@algorithmx.org)
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

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Bindings with Scopes. Allows the user to create/remove scopes around the Bindings. Each Binding is tied to a
 * Scope and Binding is removed once the Scope is removed. Binding is treated much like a Local variable on a method stack.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public interface ScopedBindings extends Bindings {

    /**
     * Returns the current working scope.
     *
     * @return working scope.
     */
    Bindings getCurrentScope();

    /**
     * Returns all the available Scopes (in order which they were added).
     *
     * @return all available Scopes.
     */
    Iterable<Bindings> getScopes();

    /**
     * Returns all the available Scopes (in the opposite order which they were added).
     *
     * @return all available Scopes (in reverse order).
     */
    Iterable<Bindings> getScopesInReverseOrder();

    /**
     * Creates new scope and pushes it to the top of Stack.
     *
     * @return the newly created Bindings.
     */
    Bindings newScope();

    /**
     * Pushed the given scope to the top of Stack.
     *
     * @param bindings existing bindings.
     * @return the newly scoped Bindings.
     */
    Bindings newScope(Bindings bindings);

    /**
     * Pops the working Bindings off the Stack.
     *
     * @return the removed Bindings.
     */
    Bindings endScope();

    /**
     * Retrieves the Binding identified by the given name. The search starts with working scope and goes back the Stack
     * until the initial scope. The search stops once a match is found.
     *
     * @param name name of the Binding.
     * @param <T> generic type of the Binding.
     * @return Binding if found; null otherwise.
     */
    @Override
    default <T> Binding<T> getBinding(String name) {
        Binding<T> result = null;

        for (Bindings scope : getScopes()) {
            result = scope.getBinding(name);
            if (result != null) break;
        }

        return result;
    }

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
    default <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        Binding<T> result = null;

        for (Bindings scope : getScopes()) {
            result = scope.getBinding(name, type);
            if (result != null) break;
        }

        return result;
    }

    /**
     * Retrieves all the Bindings of the given type. The search starts with working scope and goes back the Stack
     * until the initial scope. The search stops once a match is found.
     *
     * @param type desired type.
     * @param <T> generic type of the Binding.
     * @return all matching Bindings.
     */
    @Override
    default <T> Set<Binding<T>> getBindings(TypeReference<T> type) {
        Set<Binding<T>> result = new HashSet<>();

        for (Bindings scope : getScopesInReverseOrder()) {
            result.addAll(scope.getBindings(type));
        }

        return result;
    }

    /**
     * Retrieves the Binding values as an Unmodifiable Map. The retrieval starts with working scope and goes back the Stack
     * until the initial scope.
     *
     * @return unmodifiable Map of the Binding values.
     */
    @Override
    default Map<String, ?> asMap() {
        Map<String, Object> result = new HashMap<>();

        for (Iterator<Binding<?>> it = iterator(); it.hasNext();) {
            Binding<?> binding = it.next();
            result.put(binding.getName(), binding.getValue());
        }

        return result;
    }

    /**
     * Retrieves the number of Bindings in all the scopes. All Bindings are accounted for (does not account for unique names).
     *
     * @return total number of Bindings (in all Scopes).
     */
    @Override
    default int size() {
        int result = 0;

        for (Bindings scope : getScopes()) {
            result += scope.size();
        }

        return result;
    }

    /**
     * Retrieves the number of unique (by name) Bindings in all the scopes.
     *
     * @return unique Bindings count.
     */
    default int uniqueSize() {
        return asMap().size();
    }

    /**
     * Iterator of all the Bindings starting withe working scope and going up the Stack.
     *
     * @return all bindings (reverse order).
     */
    default Iterator<Binding<?>> iterator() {
        Set<Binding<?>> result = new HashSet<>();

        for (Bindings scope : getScopesInReverseOrder()) {
            for (Iterator<Binding<?>> it = scope.iterator(); it.hasNext();) {
                result.add(it.next());
            }
        }

        return result.iterator();
    }

    /**
     * Declares a new Binding given a name, type and an initial value in the current working scope.
     *
     * @param name name of the Binding.
     * @param type type reference of the Binding.
     * @param initialValue initial value of the Binding.
     * @param mutable determines whether the value is mutable.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     * @throws org.algorithmx.rules.bind.InvalidBindingException thrown if we cannot set initial value.
     * @see Binding
     */
    @Override
    default <T> Bindings bind(String name, TypeReference type, T initialValue, boolean mutable)
            throws BindingAlreadyExistsException, InvalidBindingException {
        getCurrentScope().bind(name, type, initialValue, mutable);
        return this;
    }

    /**
     * Binds the given Binding into the current scope. Follows the same rules as adding a new Binding with name, type, etc.
     *
     * @param binding existing Binding.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if the Binding already exists.
     */
    @Override
    default <T> Bindings bind(Binding<T> binding) {
        getCurrentScope().bind(binding);
        return this;
    }

    /**
     * Binds all the given Bindings into current scope. Follows the same rules as adding a new Binding with name, type, etc.
     * The execution will stop if a Binding already exists.
     *
     *
     * @param bindings existing Bindings.
     * @param <T> generic type of the Binding.
     * @return this Bindings (fluent interface).
     * @throws org.algorithmx.rules.bind.BindingAlreadyExistsException thrown if a Binding already exists.
     */
    @Override
    default <T> Bindings bind(Collection<Binding<T>> bindings) {
        getCurrentScope().bind(bindings);
        return this;
    }
}
