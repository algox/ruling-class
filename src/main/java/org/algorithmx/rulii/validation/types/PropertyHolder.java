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

package org.algorithmx.rulii.validation.types;

import org.algorithmx.rulii.core.UnrulyException;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;

public class PropertyHolder implements SourceHolder<PropertyDescriptor> {

    private final PropertyDescriptor property;
    private final AnnotatedTypeDefinition definition;

    public PropertyHolder(PropertyDescriptor property, AnnotatedTypeDefinition definition) {
        super();
        Assert.notNull(property, "property cannot be null.");
        Assert.notNull(property.getReadMethod(), "getter cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        this.property = property;
        this.definition = definition;
    }

    @Override
    public PropertyDescriptor getSource() {
        return property;
    }

    public AnnotatedTypeDefinition getDefinition() {
        return definition;
    }

    @Override
    public String getName() {
        return property.getReadMethod().getDeclaringClass().getSimpleName() + "." + property.getName();
    }

    @Override
    public SourceHolder<PropertyDescriptor> copy(AnnotatedTypeDefinition definition) {
        return new PropertyHolder(property, definition);
    }

    public Object getValue(Object parent) {
        try {
            return property.getReadMethod().invoke(parent);
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new UnrulyException("Unable to get field value for field [" + property.getName()
                    + "] on class [" + property.getReadMethod().getDeclaringClass().getName() + "]", e);
        }
    }

    @Override
    public String toString() {
        return "PropertyHolder{" +
                "property=" + property +
                ", definition=" + definition +
                '}';
    }
}
