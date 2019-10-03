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
package org.algorithmx.rules.bind.impl;

import org.algorithmx.rules.bind.Binding;
import org.algorithmx.rules.bind.InvalidBindingException;
import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.spring.util.ClassUtils;
import org.algorithmx.rules.spring.util.TypeUtils;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Pattern;

/**
 * Binding is a mapping between a name and a value.
 *
 * @param <T> generic type of the Binding.
 *
 * @author Max Arulananthan
 * @since 1.0
 * @see org.algorithmx.rules.bind.Binding
 */
class DefaultBinding<T> implements Binding<T> {

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private final String name;
    private final Type type;
    private final Predicate<T> validationCheck;

    private Supplier<T> valueSupplier;
    private boolean mutable = true;

    /**
     * Creates a new DefaultBinding using the given Supplier. Supplier is responsible to supplying the Binding's value.
     * The Binding will not enforce any validation rules on the Binding nor will it be mutable.
     *
     * @param name name of the Binding.
     * @param type Type of the Binding.
     * @param supplier value supplier.
     */
    DefaultBinding(String name, Type type, Supplier<T> supplier) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "Binding name must match [" + NAME_PATTERN + "] Given [" + name + "]");
        Assert.notNull(type, "type cannot be null");
        this.name = name;
        this.type = type;
        this.validationCheck = null;
        this.valueSupplier = supplier;
        setMutable(false);
    }

    /**
     * Creates a new DefaultBinding
     *
     * @param name name of the Binding.
     * @param type Type of the Binding.
     * @param value initial value of the Binding.
     * @param validationCheck any validation checks to be performed on the value.
     */
    DefaultBinding(String name, Type type, T value, Predicate<T> validationCheck) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "Binding name must match [" + NAME_PATTERN + "]");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.notNull(type, "type cannot be null");
        this.name = name;
        this.type = type;
        this.validationCheck = validationCheck;
        setValue(value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public T getValue() {
        return valueSupplier.get();
    }

    @Override
    public boolean isMutable() {
        return mutable;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(T value) {

        // Make sure we can edit this value
        if (!isMutable()) {
            throw new InvalidBindingException("Attempting to change a immutable Binding [" + name + "]");
        }

        // Looks like they are setting a null value into a required Binding
        if (validationCheck != null && !validationCheck.test(value)) {
            throw new InvalidBindingException("Validation Rule failed while attempting to set an Invalid value ["
                    + value + "] on Binding [" + name + "] Type [" + type + "]");
        }

        // Looks like they are passing us a wrong value type
        if (value != null && !isTypeAcceptable(value.getClass())) {
            throw new InvalidBindingException(name, type, value);
        }

        if (value == null && (type instanceof Class) && ((Class) type).isPrimitive()) {
            value = (T) ClassUtils.getDefaultValue((Class) type);
        }

        this.valueSupplier = new ValueSupplier<>(value);
    }

    @Override
    public boolean isTypeAcceptable(Type type) {
        return TypeUtils.isAssignable(this.type, type);
    }

    @Override
    public boolean isAssignable(Type type) {
        return TypeUtils.isAssignable(type, this.type);
    }

    /**
     * Add the ability to control mutability of this Binding.
     * @param mutable determines whether this Binding can be edited.
     */
    void setMutable(boolean mutable) {
        this.mutable = mutable;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultBinding<?> that = (DefaultBinding<?>) o;
        return name.equals(that.name) &&
                type.equals(that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        Object value = valueSupplier.get();
        return "Binding {" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + (value instanceof Binding ? "" : (value != null ? value.toString() : "null")) +
                '}';
    }

    /**
     * Internal class to act as the supplier for cases where the value is provided.
     *
     * @param <T> generic Binding type.
     */
    private static class ValueSupplier<T> implements Supplier<T> {

        private final AtomicReference<T> ref;

        ValueSupplier(T value) {
            super();
            this.ref = new AtomicReference<>(value);
        }

        @Override
        public T get() {
            return ref.get();
        }

        void setValue(T value) {
            this.ref.set(value);
        }
    }
}
