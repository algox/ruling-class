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
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.util.TypeReference;

import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        return getTarget().getBinding(name);
    }

    @Override
    public <T> T getValue(String name) {
        return getTarget().getValue(name);
    }

    @Override
    public <T> void setValue(String name, T value) {
        getTarget().setValue(name, value);
    }

    @Override
    public <T> Binding<T> getBinding(String name, Class<T> type) {
        return getTarget().getBinding(name, type);
    }

    @Override
    public <T> Binding<T> getBinding(String name, TypeReference<T> type) {
        return getTarget().getBinding(name, type);
    }

    @Override
    public <T> Set<Binding<T>> getBindings(Class<T> type) {
        return getTarget().getBindings(type);
    }

    @Override
    public <T> Set<Binding<T>> getBindings(TypeReference<T> type) {
        return getTarget().getBindings(type);
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
    public <S extends Bindings, T> S bindProperties(T bean) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings, T> S bindFields(T bean) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <S extends Bindings> S bindMap(Map<String, ?> map) {
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
}
