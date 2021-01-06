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

import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;
import org.algorithmx.rules.util.TypeReference;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

/**
 * Default implementation of the Scoped Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultScopedBindings implements ScopedBindings {

    private final Stack<Bindings> scopes = new Stack<>();

    DefaultScopedBindings() {
        super();
        init();
    }

    DefaultScopedBindings(Bindings bindings) {
        super();
        Assert.notNull(bindings, "bindings cannot be null.");
        this.scopes.push(bindings);
    }

    @Override
    public Bindings addScope() {
        Bindings result = createScope();
        scopes.push(result);
        return result;
    }

    @Override
    public Bindings removeScope() {
        // Check to make sure we are not removing the root scope
        if (scopes.size() == 1) {
            throw new CannotRemoveRootScopeException();
        }

        return scopes.pop();

    }

    public Bindings removeScope(Bindings target) {
        // Check to make sure we are not removing the root scope
        if (scopes.size() == 1) {
            throw new CannotRemoveRootScopeException();
        }

        boolean found = false;
        for (Bindings bindings : scopes) {
            // Compare the reference to make sure we match.
            if (bindings == target) {
                found = true;
                break;
            }
        }

        if (!found) {

        }

        Bindings result;

        // We know the Scope exists;
        do {
            // Pop will we find out target
            result = removeScope();
        } while (result != target);

        return result;
    }

    @Override
    public Bindings getRootScope() {
        return scopes.get(0);
    }

    @Override
    public <T> Binding<T> getBinding(String name) {
        Bindings[] scopes = getScopes();

        if (scopes.length == 0) return null;

        Binding<T> result = null;
        // Must start at end and come up
        for (int i = scopes.length - 1; i >=0; i--) {
            result = scopes[i].getBinding(name);
            if (result != null) break;
        }

        return result;
    }

    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        Bindings[] scopes = getScopes();

        Binding<T> result = null;
        // Must start at end and come up
        for (int i = scopes.length - 1; i >=0; i--) {
            result = scopes[i].getBinding(name, type);
            if (result != null) break;
        }

        return result;
    }

    @Override
    public <T> Map<String, Binding<T>> getBindings(TypeReference<T> type) {
        Map<String, Binding<T>> result = new HashMap<>();
        Bindings[] scopes = getScopes();

        // Must start at end and come up
        for (int i = scopes.length - 1; i >=0; i--) {
            result.putAll(scopes[i].getBindings(type));
            // Found something in this scope stop.
            if (result.size() > 0) break;
        }

        return result;
    }

    @Override
    public <T> Map<String, Binding<T>> getAllBindings(TypeReference<T> type) {
        Map<String, Binding<T>> result = new HashMap<>();
        Bindings[] scopes = getScopes();

        // Must start at root and keep adding
        for (Bindings scope : scopes) {
            result.putAll(scope.getBindings(type));
        }

        return result;
    }

    @Override
    public Bindings getCurrentScope() {
        return scopes.peek();
    }

    @Override
    public int size() {
        Bindings[] scopes = getScopes();
        int result = 0;

        for (Bindings scope : scopes) {
            result += scope.size();
        }

        return result;
    }

    @Override
    public Map<String, ?> asMap() {
        Bindings[] scopes = getScopes();
        Map<String, Object> result = new HashMap<>();

        for (Bindings scope : scopes) {
            result.putAll(scope.asMap());
        }

        return result;
    }

    /**
     * Iterator of all the Bindings starting with working scope and going up the Stack.
     *
     * @return all bindings (reverse order).
     */
    public Iterator<Binding<?>> iterator() {
        Bindings[] scopes = getScopes();
        Set<Binding<?>> result = new HashSet<>();

        // Must start at root and keep adding
        for (Bindings scope : scopes) {
            for (Iterator<Binding<?>> it = scope.iterator(); it.hasNext();) {
                result.add(it.next());
            }
        }

        return result.iterator();
    }

    /**
     * Creates a new scope and pushes it into the Stack.
     *
     */
    protected void init() {
        // Add the root scope
        scopes.push(createScope());
    }

    /**
     * Creates a new scope.
     *
     * @return newly created Bindings.
     */
    protected Bindings createScope() {
        return Bindings.create();
    }

    private Bindings[] getScopes() {
        return scopes.toArray(new Bindings[scopes.size()]);
    }

    @Override
    public String prettyPrint(String prefix) {
        StringBuilder result = new StringBuilder();
        int scopeIndex = 0;

        for (Bindings scope : scopes) {
            result.append("Scope (index = " + (scopeIndex++) + ")");
            result.append(prefix + scope.prettyPrint(getTabs(scopeIndex + 1)));
            result.append(prefix + getTabs(scopeIndex));
        }

        return result.toString();
    }

    private String getTabs(int count) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < count; i++) {
            result.append(RuleUtils.TAB);
        }

        return result.toString();
    }

    @Override
    public String toString() {
        return prettyPrint("");
    }
}
