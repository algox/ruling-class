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

import org.algorithmx.rulii.lib.spring.util.Assert;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class AnnotatedBeanTypeDefinition {

    private final Class<?> type;
    private final BeanInfo beanInfo;

    private final Class<? extends Annotation> introspectionAnnotationType;
    private final Class<? extends Annotation> markerAnnotationType;

    private final Map<Field, FieldHolder> fieldDefinitions;
    private final Map<PropertyDescriptor, PropertyHolder> propertyDefinitions;
    private final Map<Method, MethodHolder> methodDefinitions;

    public AnnotatedBeanTypeDefinition(Class<?> type, BeanInfo beanInfo,
                                       Class<? extends Annotation> introspectionAnnotationType,
                                       Class<? extends Annotation> markerAnnotationType,
                                       Map<Field, FieldHolder> fieldDefinitions,
                                       Map<PropertyDescriptor, PropertyHolder> propertyDefinitions,
                                       Map<Method, MethodHolder> methodDefinitions) {
        super();
        Assert.notNull(type, " type cannot be null.");
        Assert.notNull(beanInfo, " beanInfo cannot be null.");
        Assert.notNull(introspectionAnnotationType, " introspectionAnnotationType cannot be null.");
        Assert.notNull(markerAnnotationType, " markerAnnotationType cannot be null.");
        Assert.notNull(fieldDefinitions, " fieldDefinitions cannot be null.");
        Assert.notNull(propertyDefinitions, " propertyDefinitions cannot be null.");
        Assert.notNull(methodDefinitions, " methodDefinitions cannot be null.");
        this.introspectionAnnotationType = introspectionAnnotationType;
        this.markerAnnotationType = markerAnnotationType;
        this.type = type;
        this.beanInfo = beanInfo;
        this.fieldDefinitions = fieldDefinitions;
        this.propertyDefinitions = propertyDefinitions;
        this.methodDefinitions = methodDefinitions;
    }

    public FieldHolder getFieldDefinition(Field field) {
        return fieldDefinitions.get(field);
    }

    public PropertyHolder getPropertyDefinition(PropertyDescriptor descriptor) {
        return propertyDefinitions.get(descriptor);
    }

    public AnnotatedTypeDefinition getMethodDefinition(Method method) {
        MethodHolder holder = methodDefinitions.get(method);
        return holder != null ? holder.getDefinition() : null;
    }

    public FieldHolder[] getFields() {
        Collection<FieldHolder> result = fieldDefinitions.values();
        return result.toArray(new FieldHolder[result.size()]);
    }

    public PropertyHolder[] getProperties() {
        Collection<PropertyHolder> result = propertyDefinitions.values();
        return result.toArray(new PropertyHolder[result.size()]);
    }

    public MethodHolder[] getMethods() {
        Collection<MethodHolder> result = methodDefinitions.values();
        return result.toArray(new MethodHolder[result.size()]);
    }


    public AnnotatedTypeDefinition getMethodParameterDefinition(Method method, int index) {
        MethodHolder holder = methodDefinitions.get(method);
        return holder != null ? holder.getParameterTypeDefinition(index) : null;
    }

    public Class<?> getType() {
        return type;
    }

    public BeanInfo getBeanInfo() {
        return beanInfo;
    }

    public Class<? extends Annotation> getIntrospectionAnnotationType() {
        return introspectionAnnotationType;
    }

    public Class<? extends Annotation> getMarkerAnnotationType() {
        return markerAnnotationType;
    }

    @Override
    public String toString() {
        return "AnnotatedBeanTypeDefinition{" +
                "type=" + type +
                ", beanInfo=" + beanInfo +
                ", introspectionAnnotationType=" + introspectionAnnotationType +
                ", markerAnnotationType=" + markerAnnotationType +
                ", fieldDefinitions=" + fieldDefinitions +
                ", propertyDefinitions=" + propertyDefinitions +
                ", methodDefinitions=" + methodDefinitions +
                '}';
    }
}
