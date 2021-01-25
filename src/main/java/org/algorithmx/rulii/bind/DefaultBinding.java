/*
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

package org.algorithmx.rulii.bind;

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.util.RuleUtils;

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
 * @see Binding
 */
public class DefaultBinding<T> implements Binding<T> {

    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private final String name;
    private final Type type;
    private final AtomicReference<T> value = new AtomicReference<>();
    private final boolean primary;
    // Cannot be final as change it to editable = false after we set the value in the ctor
    private boolean editable = true;
    private String description = null;

    /**
     * Creates a new DefaultBinding
     *
     * @param name name of the Binding.
     * @param type Type of the Binding.
     * @param value initial value of the Binding.
     * @param editable determines whether this Binding is editable or not.
     * @param primary determines whether this Binding is a Primary candidate or not.
     */
    DefaultBinding(String name, Type type, T value, boolean editable, boolean primary, String description) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.notNull(type, "type cannot be null");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.isTrue(NAME_PATTERN.matcher(name).matches(), "Binding name [" + name + "] must match [" + NAME_PATTERN + "]");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        this.name = name;
        this.type = type;
        setValue(value);
        this.editable = editable;
        this.primary = primary;
        this.description = description;
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
        if (value != null && !isAssignable(value.getClass())) {
            throw new InvalidBindingException(name, type, value);
        }

        this.value.set(value);
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getTypeName() {
        if (type == null) return null;
        if (type instanceof Class) return ((Class) type).getSimpleName();
        return type.getTypeName();
    }

    @Override
    public String getTypeAndName() {
        return getTypeName() + " " + name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Binding<?> that = (Binding<?>) o;
        return name.equals(that.getName()) &&
                type.equals(that.getType()) &&
                Objects.equals(value, that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    @Override
    public String getSummary() {
        return "(" + getTypeAndName() + " = " + value + ")";
    }

    @Override
    public String toString() {
        return "Name = " + name +
                ", Type = " + getTypeName() +
                ", Value = " + RuleUtils.getTextValue(value, 80) +
                ", Primary = " + primary +
                ", Editable = " + editable +
                ", Description = " + description;
    }
}
