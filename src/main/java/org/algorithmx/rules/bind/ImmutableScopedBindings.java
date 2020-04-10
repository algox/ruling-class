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
 * Immutable version of the ScopedBindings. All functions that create Bindings/Scopes will be disabled.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class ImmutableScopedBindings extends ImmutableBindings implements ScopedBindings {

    ImmutableScopedBindings(ScopedBindings bindings) {
        super(bindings);
    }

    @Override
    protected ScopedBindings getTarget() {
        return (ScopedBindings) super.getTarget();
    }

    @Override
    public Bindings getCurrentScope() {
        return getTarget().getCurrentScope();
    }

    @Override
    public Bindings addScope() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Bindings removeScope() {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Map<String, Binding<T>> getAllBindings(TypeReference<T> type) {
        return getTarget().getAllBindings(type);
    }

    @Override
    public String toString() {
        return getTarget().toString();
    }
}
