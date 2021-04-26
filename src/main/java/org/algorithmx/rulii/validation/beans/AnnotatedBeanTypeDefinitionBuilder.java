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

package org.algorithmx.rulii.validation.beans;

import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.lib.spring.util.ReflectionUtils;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinition;
import org.algorithmx.rulii.validation.types.AnnotatedTypeDefinitionBuilder;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Predicate;

public class AnnotatedBeanTypeDefinitionBuilder {

    private final Class<?> type;
    private final BeanInfo beanInfo;

    private final Class<? extends Annotation> introspectionAnnotationType;
    private final Class<? extends Annotation> markerAnnotationType;

    private final Map<Field, FieldHolder> fieldDefinitions = new LinkedHashMap<>();
    private final Map<PropertyDescriptor, PropertyHolder> propertyDefinitions = new LinkedHashMap<>();
    private final Map<Method, MethodHolder> methodDefinitions = new LinkedHashMap<>();

    private AnnotatedBeanTypeDefinitionBuilder(Class<?> type, Class<? extends Annotation> introspectionAnnotationType,
                                               Class<? extends Annotation> markerAnnotationType) {
        super();
        Assert.notNull(type, " type cannot be null.");
        Assert.notNull(introspectionAnnotationType, " introspectionAnnotationType cannot be null.");
        Assert.notNull(markerAnnotationType, " markerAnnotationType cannot be null.");
        this.type = type;
        this.beanInfo = org.algorithmx.rulii.util.reflect.ReflectionUtils.loadBeanInfo(type);
        this.introspectionAnnotationType = introspectionAnnotationType;
        this.markerAnnotationType = markerAnnotationType;
    }

    public static AnnotatedBeanTypeDefinitionBuilder with(Class<?> type,
                                                          Class<? extends Annotation> introspectionAnnotationType,
                                                          Class<? extends Annotation> markerAnnotationType) {
        return new AnnotatedBeanTypeDefinitionBuilder(type, introspectionAnnotationType, markerAnnotationType);
    }

    public AnnotatedBeanTypeDefinitionBuilder field(Field field, AnnotatedTypeDefinition definition) {
        Assert.notNull(field, "field cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        fieldDefinitions.put(field, new FieldHolder(field, definition));
        return this;
    }

    public AnnotatedBeanTypeDefinitionBuilder property(PropertyDescriptor descriptor, AnnotatedTypeDefinition definition) {
        Assert.notNull(descriptor, "descriptor cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        propertyDefinitions.put(descriptor, new PropertyHolder(descriptor, definition));
        return this;
    }

    public AnnotatedBeanTypeDefinitionBuilder method(Method method, AnnotatedTypeDefinition definition) {
        Assert.notNull(method, "method cannot be null.");
        Assert.notNull(definition, "definition cannot be null.");
        methodDefinitions.put(method, new MethodHolder(method, definition));
        return this;
    }

    public AnnotatedBeanTypeDefinitionBuilder methodParameter(Method method, int index, AnnotatedTypeDefinition definition) {
        Assert.notNull(method, "method cannot be null.");
        Assert.isTrue(index >= 0, "index must be >= 0");
        Assert.notNull(definition, "definition cannot be null.");

        MethodHolder methodHolder = methodDefinitions.get(method);

        if (methodHolder == null) throw new IllegalStateException("setup method(Method method, AnnotatedTypeDefinition definition) first.");

        methodHolder.setParameter(index, definition);
        return this;
    }

    public AnnotatedBeanTypeDefinitionBuilder loadFields(Predicate<Field> filter, Comparator<Field> comparator) {
        Set<Field> fields = comparator != null ? new TreeSet<>(comparator) : new HashSet<>();

        ReflectionUtils.doWithFields(type,
                field -> {
                        boolean add = filter != null ? filter.test(field) : true;
                        if (add) fields.add(field);
                });

        fields.stream().forEach(
                field -> {
                    //System.err.println("XXX Field [" + field.getName() + "]");
                    AnnotatedTypeDefinitionBuilder builder = AnnotatedTypeDefinitionBuilder
                            .with(field.getAnnotatedType(), introspectionAnnotationType, markerAnnotationType);
                    field(field, builder.build());
                });

        return this;
    }

    public AnnotatedBeanTypeDefinitionBuilder loadProperties(Predicate<PropertyDescriptor> filter,
                                                             Comparator<PropertyDescriptor> comparator) {
        Set<PropertyDescriptor> properties = comparator != null
                ? new TreeSet<>(comparator)
                : new HashSet<>();

        properties.addAll(Arrays.asList(beanInfo.getPropertyDescriptors()));

        properties.stream()
                .filter(d -> filter != null ? filter.test(d) : true)
                .forEach(d -> {
                    AnnotatedTypeDefinitionBuilder builder = AnnotatedTypeDefinitionBuilder
                            .with(d.getReadMethod().getAnnotatedReturnType(),
                            introspectionAnnotationType, markerAnnotationType);
                    property(d, builder.build());
                });

        return this;
    }

    public AnnotatedBeanTypeDefinitionBuilder loadMethods(Predicate<Method> filter, Comparator<Method> comparator) {
        Set<Method> getters = new HashSet<>();

        Arrays.stream(beanInfo.getPropertyDescriptors())
                .filter(d -> d.getReadMethod() != null)
                .forEach(d -> getters.add(d.getReadMethod()));

        Set<Method> methods = comparator != null ? new TreeSet<>(comparator) : new HashSet<>();

        ReflectionUtils.doWithMethods(type,
                method -> {
                    boolean add = !getters.contains(method) && (filter != null ? filter.test(method) : true);
                    if (add) methods.add(method);
                });

        methods.stream()
                .forEach(method -> {
                    AnnotatedTypeDefinitionBuilder builder = AnnotatedTypeDefinitionBuilder
                            .with(method.getAnnotatedReturnType(), introspectionAnnotationType, markerAnnotationType);

                    method(method, builder.build());

                    AnnotatedType[] annotatedTypes = method.getAnnotatedParameterTypes();

                    for (int i = 0; i < annotatedTypes.length; i++) {
                        AnnotatedTypeDefinitionBuilder parameterDefinitionBuilder = AnnotatedTypeDefinitionBuilder
                                .with(annotatedTypes[i], introspectionAnnotationType, markerAnnotationType);
                        methodParameter(method, i, parameterDefinitionBuilder.build());
                    }
                });

        return this;
    }

    public AnnotatedBeanTypeDefinition build() {
        return new AnnotatedBeanTypeDefinition(type, beanInfo, introspectionAnnotationType,
                markerAnnotationType, fieldDefinitions, propertyDefinitions, methodDefinitions);
    }
}
