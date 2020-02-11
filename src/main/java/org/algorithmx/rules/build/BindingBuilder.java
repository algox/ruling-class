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
package org.algorithmx.rules.build;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.TypeReference;
import org.algorithmx.rules.bind.impl.DefaultBinding;
import org.algorithmx.rules.spring.util.Assert;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Builder class for creating Bindings.
 *
 * @author Max Arulananthan
 * @since 1.0
 */
public class BindingBuilder {

    private final String name;
    private TypeReference typeRef = TypeReference.with(Object.class);
    private Object value = null;
    private boolean mutable = true;

    /**
     * Private Ctor taking the Binding name.
     *
     * @param name Binding name.
     */
    private BindingBuilder(String name) {
        super();
        this.name = name;
    }

    /**
     * Created a new builder with the Binding name.
     *
     * @param name Binding name.
     * @return new Binding Builder.
     */
    public static BindingBuilder name(String name) {
        return new BindingBuilder(name);
    }

    /**
     * Type of the Bindings.
     *
     * @param type type of the Binding.
     * @return this for fluency.
     */
    public BindingBuilder type(Class<?> type) {
        this.typeRef = TypeReference.with(type);
        return this;
    }

    /**
     * Type of the Bindings.
     *
     * @param typeRef type of the Binding.
     * @return this for fluency.
     */
    public BindingBuilder type(TypeReference typeRef) {
        Assert.notNull(typeRef, "typeRef cannot be null");
        this.typeRef = typeRef;
        return this;
    }

    /**
     * Value of the Bindings.
     *
     * @param value value of the Binding.
     * @return this for fluency.
     */
    public BindingBuilder value(Object value) {
        this.value = value;
        return this;
    }

    /**
     * Value of the Bindings.
     *
     * @param supplier value supplier of the Binding. supplier.get() will be called immediately.
     * @param <T> generic type of the supplier.
     * @return this for fluency.
     */
    public <T> BindingBuilder value(Supplier<T> supplier) {
        Assert.notNull(supplier, "supplier cannot be null");
        this.value = supplier.get();
        return this;
    }

    /**
     * Value of the Bindings.
     *
     * @param supplier value supplier of the Binding. If supplier.get() throws a NullPointerException then the
     *                 value will be null.
     * @param <T> generic type of the supplier.
     * @return this for fluency.
     */
    public <T> BindingBuilder nullSafeValue(Supplier<T> supplier) {
        return nullSafeValue(supplier, null);
    }

    /**
     * Value of the Bindings.
     *
     * @param supplier value supplier of the Binding. If supplier.get() throws a NullPointerException then the
     *                 value will use the defaultValue.
     * @param defaultValue value to use in case if the Supplier throws a NullPointerException.
     * @param <T> generic type of the supplier.
     * @return this for fluency.
     */
    public <T> BindingBuilder nullSafeValue(Supplier<T> supplier, T defaultValue) {
        Assert.notNull(supplier, "supplier cannot be null");

        try {
            this.value = supplier.get();
        } catch (NullPointerException e) {
            this.value = defaultValue;
        }
        
        return this;
    }

    /**
     * Value of the Bindings.
     *
     * @param optionalValue optional value of the Binding. optionalValue.get() will be called immediately.
     * @param <T> generic type of the Optional value.
     * @return this for fluency.
     */
    public <T> BindingBuilder value(Optional<T> optionalValue) {
        Assert.notNull(optionalValue, "optionalValue cannot be null");
        this.value = optionalValue.isPresent() ? optionalValue.get() : null;
        return this;
    }

    /**
     * Determines whether the Binding is editable or not.
     *
     * @param mutable mutability of the Binding.
     * @return this for fluency.
     */
    public BindingBuilder mutable(boolean mutable) {
        this.mutable = mutable;
        return this;
    }

    /**
     * Create a Binding with the given properties.
     * @param <T> desired Type.
     * @return new Binding.
     */
    public <T> Binding<T> create() {
        return new DefaultBinding(name, typeRef.getType(), value, mutable);
    }
}
