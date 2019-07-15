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
import java.util.function.Predicate;

/**
 * Binding is a mapping between a name and a value.
 *
 * @author Max Arulananthan
 * @Since 1.0
 * @See org.algorithmx.rules.bind.Binding
 */
class SimpleBinding<T> implements Binding<T> {

    private final String name;
    private final Type type;
    private final Predicate validationCheck;

    private T value;
    private boolean mutable = true;

    /**
     * Creates a new SimpleBinding
     *
     * @param name name of the Binding.
     * @param type Type of the Binding.
     * @param value initial value of the Binding.
     * @param validationCheck any validation checks to be performed on the value.
     */
    SimpleBinding(String name, Type type, T value, Predicate validationCheck) {
        super();
        Assert.notNull(name, "name cannot be null");
        Assert.isTrue(name.trim().length() > 0, "name length must be > 0");
        Assert.notNull(type, "type cannot be null");
        this.name = name;
        this.type = type;
        this.validationCheck = validationCheck;
        setValue(value);
    }

    /**
     * Name of the Binding.
     *
     * @return name.
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Type of the Binding.
     *
     * @return type.
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * Value of the Binding.
     *
     * @return value.
     */
    @Override
    public T getValue() {
        return value;
    }

    /**
     * Determines whether this Binding is modifiable.
     *
     * @return true if modifiable; false otherwise.
     */
    @Override
    public boolean isMutable() {
        return mutable;
    }

    /**
     * Sets the value of the Binding.
     *
     * @param value new value.
     *
     * @throws InvalidBindingException thrown if the value doesn't pass the validation check
     * or if there is type mismatch. The type checking simply makes sure that the value passed matches the declared type
     * of the Binding. It will not check any generics similar to Java. If the Binding is declared as List<Integer> and
     * the value that is passed is a List<String>. InvalidBindingException will NOT be thrown in this case.
     */
    @Override
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

        this.value = value;
    }

    /**
     * Determines whether the given type is acceptable.
     *
     * @param type input type.
     * @return true if the given type matches the SimpleBinding type.
     */
    @Override
    public boolean isTypeAcceptable(Type type) {
        return TypeUtils.isAssignable(this.type, type);
    }

    /**
     * Determines whether the binding can be assigned to the given Type.
     *
     * @param type desired type.
     * @return true if this Binding can be assigned to the desired type.
     */
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
        SimpleBinding that = (SimpleBinding) o;
        return name.equals(that.name) &&
                type.equals(that.type) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    @Override
    public String toString() {
        return "SimpleBinding {" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
