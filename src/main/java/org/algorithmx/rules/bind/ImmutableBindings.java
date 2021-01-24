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

import org.algorithmx.rules.bind.load.BindingLoader;
import org.algorithmx.rules.lib.spring.util.Assert;
import org.algorithmx.rules.util.TypeReference;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Immutable version of the Bindings. All functions that create Bindings will be disabled.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ImmutableBindings implements Bindings {

    private final Bindings target;

    ImmutableBindings(Bindings target) {
        super();
        Assert.notNull(target, "target cannot be null.");
        this.target = target;
    }

    @Override
    public int size() {
        return getTarget().size();
    }

    @Override
    public boolean contains(String name) {
        return getTarget().contains(name);
    }

    @Override
    public <T> boolean contains(String name, Class<T> type) {
        return getTarget().contains(name, type);
    }

    @Override
    public <T> boolean contains(String name, TypeReference<T> type) {
        return getTarget().contains(name, type);
    }

    @Override
    public <T> Binding<T> getBinding(String name) {
        Binding<T> result = getTarget().getBinding(name);
        return result != null ? result.immutableSelf() : null;
    }

    @Override
    public <T> T getValue(String name) {
        return getTarget().getValue(name);
    }

    @Override
    public <T> void setValue(String name, T value) {
        throw new IllegalStateException("Binding [" + name + "] is immutable. It cannot be edited.");
    }

    @Override
    public <T> Binding<T> getBinding(String name, Class<T> type) {
        Binding<T> result = getTarget().getBinding(name, type);
        return result != null ? result.immutableSelf() : null;
    }

    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        Binding<T> result = getTarget().getBinding(name, type);
        return result != null ? result.immutableSelf() : null;
    }

    @Override
    public <T> Map<String, Binding<T>> getBindings(Class<T> type) {
        return convertToImmutableMap(getTarget().getBindings(type));
    }

    @Override
    public <T> Map<String, Binding<T>> getBindings(TypeReference<T> type) {
        return convertToImmutableMap(getTarget().getBindings(type));
    }

    private <T> Map<String, Binding<T>> convertToImmutableMap(Map<String, Binding<T>> original) {
        if (original == null) return null;
        Map<String, Binding<T>> result = new HashMap<>();

        for (Map.Entry<String, Binding<T>> entry : original.entrySet()) {
            result.put(entry.getKey(), entry.getValue() != null ? entry.getValue().immutableSelf() : null);
        }

        return result;
    }

    @Override
    public Map<String, ?> asMap() {
        return getTarget().asMap();
    }

    @Override
    public Iterator<Binding<?>> iterator() {
        return getTarget().iterator();
    }

    protected Bindings getTarget() {
        return target;
    }

    @Override
    public <S extends Bindings, T> S bind(Binding<T> binding) throws BindingAlreadyExistsException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings> S bind(BindingDeclaration... declarations) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings> S bind(BindingDeclaration declaration) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bind(String name, T initialValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bind(String name, Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bind(String name, TypeReference<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bind(String name, Class<T> type, T initialValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bind(String name, TypeReference<T> type, T initialValue) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bindUsing(BindingLoader<T> loader, T value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!Bindings.class.isAssignableFrom(o.getClass())) return false;
        return target.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(target);
    }

    @Override
    public String toString() {
        return target.toString();
    }

    @Override
    public String prettyPrint(String prefix) {
        return getTarget().prettyPrint(prefix);
    }
}
