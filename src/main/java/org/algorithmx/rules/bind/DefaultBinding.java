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

import org.algorithmx.rules.spring.util.Assert;
import org.algorithmx.rules.spring.util.ClassUtils;
import org.algorithmx.rules.spring.util.TypeUtils;

import java.lang.reflect.Type;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
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
public class DefaultBinding<T> implements Binding<T> {

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private final String name;
    private final Type type;
    private final AtomicReference<T> value = new AtomicReference<>();
    private final boolean primary;
    // Cannot be final as change it to editable = false after we set the value in the ctor
    private boolean editable = true;

    /**
     * Creates a new DefaultBinding
     *
     * @param name name of the Binding.
     * @param type Type of the Binding.
     * @param value initial value of the Binding.
     * @param editable determines whether this Binding is editable or not.
     * @param primary determines whether this Binding is a Primary candidate or not.
     */
    public DefaultBinding(String name, Type type, T value, boolean editable, boolean primary) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(type, "type cannot be null");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "Binding name must match [" + NAME_PATTERN + "]");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        this.name = name;
        this.type = type;
        setValue(value);
        this.editable = editable;
        this.primary = primary;
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
        return value.get();
    }

    @Override
    public boolean isEditable() {
        return editable;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(T value) {

        // Make sure we can edit this value
        if (!isEditable()) {
            throw new InvalidBindingException("Attempting to change a immutable Binding [" + name + "]");
        }

        // Looks like they are passing us a wrong value type
        if (value != null && !isTypeAcceptable(value.getClass())) {
            throw new InvalidBindingException(name, type, value);
        }

        if (value == null && (type instanceof Class) && ((Class) type).isPrimitive()) {
            value = (T) ClassUtils.getDefaultValue((Class) type);
        }

        this.value.set(value);
    }

    @Override
    public boolean isTypeAcceptable(Type type) {
        return TypeUtils.isAssignable(this.type, type);
    }

    @Override
    public boolean isAssignable(Type type) {
        return TypeUtils.isAssignable(type, this.type);
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
        return "DefaultBinding{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                ", primary=" + primary +
                ", editable=" + editable +
                '}';
    }
}
