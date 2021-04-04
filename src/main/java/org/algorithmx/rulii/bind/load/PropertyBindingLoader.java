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

package org.algorithmx.rulii.bind.load;

import org.algorithmx.rulii.bind.BindingBuilder;
import org.algorithmx.rulii.bind.Bindings;
import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Loads the desired properties in the give Bean as separate Bindings.
 *
 * You do have the option to control which properties get added by using the (Filter/IgnoreProperties/IncludeProperties)
 * You also have the option to change the BindingName using the NameGenerator property.
 *
 * @param <T> Bean Type.
 * @author Max Arulananthan
 * @since 1.0
 */
public class PropertyBindingLoader<T> implements BindingLoader<T> {

    private Predicate<PropertyDescriptor> filter = null;
    private Function<PropertyDescriptor, String> nameGenerator = null;

    public PropertyBindingLoader() {
        super();
    }

    /**
     * Function use to change the output BindingName,
     *
     * @param nameGenerator Function that takes a Property and changes the name of the Property into the desired Binding name,
     */
    public void setNameGenerator(Function<PropertyDescriptor, String> nameGenerator) {
        this.nameGenerator = nameGenerator;
    }

    /**
     * Filter to restrict which properties become Bindings.
     *
     * @param filter restricting Filter.
     */
    public void setFilter(Predicate<PropertyDescriptor> filter) {
        this.filter = filter;
    }

    /**
     * Array of property names that won't get added.
     *
     * @param names blacklisted property names.
     */
    public void setIgnoreProperties(String...names) {
        Assert.notNull(names, "names cannot be null.");
        Set<String> nameSet = Arrays.stream(names).collect(Collectors.toSet());
        setIgnoreProperties(nameSet);
    }

    public void setIgnoreProperties(Set<String> names) {
        Assert.notNull(names, "names cannot be null.");
        this.filter = propertyDescriptor -> !names.contains(propertyDescriptor.getName());
    }

    /**
     * Array of property names that will get added.
     *
     * @param names whitelisted property names.
     */
    public void setIncludeProperties(String...names) {
        Assert.notNull(names, "names cannot be null.");
        Set<String> nameSet = Arrays.stream(names).collect(Collectors.toSet());
        setIncludeProperties(nameSet);
    }

    /**
     * Array of property names that will get Bindings.
     *
     * @param names whitelisted property names.
     */
    public void setIncludeProperties(Set<String> names) {
        Assert.notNull(names, "names cannot be null.");
        this.filter = propertyDescriptor -> names.contains(propertyDescriptor.getName());
    }

    @Override
    public void load(Bindings bindings, T bean) {
        Assert.notNull(bindings, "bindings cannot be null.");
        Assert.notNull(bean, "bean cannot be null.");

        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());

            // Go through all the properties
            for (PropertyDescriptor propertyDescriptor : beanInfo.getPropertyDescriptors()) {
                if (filter != null && !filter.test(propertyDescriptor)) continue;

                Method getterMethod = propertyDescriptor.getReadMethod();
                Method setterMethod = propertyDescriptor.getWriteMethod();

                if (getterMethod == null) continue;

                Supplier getter = () -> {
                    try {
                        return getterMethod.invoke(bean);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new UnrulyException("Unable to get property value ["
                                + getterMethod.getDeclaringClass().getSimpleName() + "." + propertyDescriptor.getName() + "]", e);
                    }
                };

                Consumer setter = (value) -> {
                    try {
                        setterMethod.invoke(bean, value);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        throw new UnrulyException("Unable to set property value ["
                                + getterMethod.getDeclaringClass().getSimpleName() + "." + propertyDescriptor.getName() + "]", e);
                    }
                };


                String bindingName = nameGenerator != null ? nameGenerator.apply(propertyDescriptor) : propertyDescriptor.getName();
                // Bind the property
                bindings.bind(BindingBuilder.with(bindingName)
                        .type(propertyDescriptor.getReadMethod().getGenericReturnType())
                        .delegate(getter, setter)
                        .editable(setterMethod != null)
                        .build());
            }
        } catch (IntrospectionException e) {
            throw new UnrulyException("Error trying to Introspect [" + bean.getClass() + "]", e);
        }
    }
}
