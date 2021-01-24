/**
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
package org.algorithmx.rules.bind;

import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.RuleUtils;
import org.algorithmx.rules.util.TypeReference;

import java.util.*;

/**
 * Default implementation of the Scoped Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class DefaultScopedBindings implements ScopedBindings {

    private final Stack<NamedScope> scopes = new Stack<>();

    DefaultScopedBindings(String name) {
        this(name, Bindings.create());
    }

    DefaultScopedBindings(String name, Bindings bindings) {
        super();
        Assert.notNull(bindings, "bindings cannot be null.");
        this.scopes.push(new NamedScope(name, bindings));
    }

    public Bindings addScope() {
        return addScope("anonymous-" + UUID.randomUUID().toString() + "-scope");
    }

    public Bindings addScope(String name) {
        Bindings result = createScope();
        addScope(name, result);
        return result;
    }

    public void addScope(String name, Bindings bindings) {
        Assert.notNull(name, "name cannot be null.");
        Assert.notNull(bindings, "bindings cannot be null.");
        Bindings existing = getScope(name);

        if (existing != null) {
            throw new BindingsAlreadyExistsException(name, existing);
        }

        scopes.push(new NamedScope(name, bindings));
    }

    public Bindings getScope(String name) {
        Bindings result = null;

        for (NamedScope scope : scopes) {
            // Compare the reference to make sure we match.
            if (scope.name.equals(name)) {
                result = scope.bindings;
                break;
            }
        }

        return result;
    }

    public String getScopeName(Bindings bindings) {
        String result = null;

        for (NamedScope scope : scopes) {
            // Compare the reference to make sure we match.
            if (scope.bindings == bindings) {
                result = scope.name;
                break;
            }
        }

        return result;
    }

    @Override
    public Bindings removeScope() {
        // Check to make sure we are not removing the root scope
        if (scopes.size() == 1) {
            throw new CannotRemoveRootScopeException();
        }

        return scopes.pop().bindings;
    }

    public Bindings removeScope(String name) {
        Bindings bindings = getScope(name);

        // We couldn't find any such Scope.
        if (bindings == null) throw new NoSuchBindingsException(name);

        return removeScope(bindings);
    }

    public Bindings removeScope(Bindings target) {
        // Check to make sure we are not removing the root scope
        if (scopes.size() == 1) {
            throw new CannotRemoveRootScopeException();
        }

        boolean found = false;
        for (NamedScope scope : scopes) {
            // Compare the reference to make sure we match.
            if (scope.bindings == target) {
                found = true;
                break;
            }
        }

        // Could not find the requested Bindings
        if (!found) {
            throw new NoSuchBindingsException(target);
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
    public Bindings getCurrentScope() {
        return scopes.peek().bindings;
    }

    public Bindings getParentScope() {
        return getScopeSize() > 2 ? scopes.get(getScopeSize() - 2).bindings : null;
    }

    @Override
    public Bindings getRootScope() {
        return scopes.get(0).bindings;
    }

    public Bindings getGlobalScope() {
        return getScope(ScopedBindings.GLOBAL_SCOPE);
    }

    @Override
    public <T> Binding<T> getBinding(String name) {
        NamedScope[] scopes = getScopes();

        if (scopes.length == 0) return null;

        Binding<T> result = null;
        // Must start at end and come up
        for (int i = scopes.length - 1; i >=0; i--) {
            result = scopes[i].bindings.getBinding(name);
            if (result != null) break;
        }

        return result;
    }

    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        NamedScope[] scopes = getScopes();

        Binding<T> result = null;
        // Must start at end and come up
        for (int i = scopes.length - 1; i >=0; i--) {
            result = scopes[i].bindings.getBinding(name, type);
            if (result != null) break;
        }

        return result;
    }

    @Override
    public <T> Map<String, Binding<T>> getBindings(TypeReference<T> type) {
        Map<String, Binding<T>> result = new HashMap<>();
        NamedScope[] scopes = getScopes();

        // Must start at end and come up
        for (int i = scopes.length - 1; i >=0; i--) {
            result.putAll(scopes[i].bindings.getBindings(type));
            // Found something in this scope stop.
            if (result.size() > 0) break;
        }

        return result;
    }

    @Override
    public <T> Map<String, Binding<T>> getAllBindings(TypeReference<T> type) {
        Map<String, Binding<T>> result = new HashMap<>();
        NamedScope[] scopes = getScopes();

        // Must start at root and keep adding
        for (NamedScope scope : scopes) {
            result.putAll(scope.bindings.getBindings(type));
        }

        return result;
    }

    public int getScopeSize() {
        return scopes.size();
    }

    @Override
    public int size() {
        NamedScope[] scopes = getScopes();
        int result = 0;

        for (NamedScope scope : scopes) {
            result += scope.bindings.size();
        }

        return result;
    }

    @Override
    public Map<String, ?> asMap() {
        NamedScope[] scopes = getScopes();
        Map<String, Object> result = new HashMap<>();

        for (NamedScope scope : scopes) {
            result.putAll(scope.bindings.asMap());
        }

        return result;
    }

    /**
     * Iterator of all the Bindings starting with working scope and going up the Stack.
     *
     * @return all bindings (reverse order).
     */
    public Iterator<Binding<?>> iterator() {
        NamedScope[] scopes = getScopes();
        Set<Binding<?>> result = new HashSet<>();

        // Must start at root and keep adding
        for (NamedScope scope : scopes) {
            for (Iterator<Binding<?>> it = scope.bindings.iterator(); it.hasNext();) {
                result.add(it.next());
            }
        }

        return result.iterator();
    }

    /**
     * Creates a new scope.
     *
     * @return newly created Bindings.
     */
    protected Bindings createScope() {
        return Bindings.create();
    }

    private NamedScope[] getScopes() {
        return scopes.toArray(new NamedScope[scopes.size()]);
    }

    @Override
    public String prettyPrint(String prefix) {
        StringBuilder result = new StringBuilder();
        int scopeIndex = 0;

        for (NamedScope scope : scopes) {
            result.append("Scope (index = " + (scopeIndex++) + ")");
            result.append(prefix + scope.bindings.prettyPrint(getTabs(scopeIndex + 1)));
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

    private static class NamedScope {
        private String name;
        private Bindings bindings;

        public NamedScope(String name, Bindings bindings) {
            Assert.notNull(name, "name cannot be null");
            Assert.notNull(bindings, "bindings cannot be null");
            this.name = name;
            this.bindings = bindings;
        }

        public String getName() {
            return name;
        }

        public Bindings getBindings() {
            return bindings;
        }

        @Override
        public String toString() {
            return "NamedScope{" +
                    "name='" + name + '\'' +
                    ", bindings=" + bindings +
                    '}';
        }
    }
}
