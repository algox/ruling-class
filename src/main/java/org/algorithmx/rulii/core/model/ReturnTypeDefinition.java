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

package org.algorithmx.rulii.core.model;

import org.algorithmx.rulii.annotation.Description;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;

public class ReturnTypeDefinition {

    private static final Map<Method, ReturnTypeDefinition> CACHE = Collections.synchronizedMap(new IdentityHashMap<>());

    private String description;
    private Type type;
    private AnnotatedType annotatedType;
    private Annotation[] annotations;

    private ReturnTypeDefinition(String description, Type type, AnnotatedType annotatedType,
                                Annotation...annotations) {
        super();
        this.description = description;
        this.type = type;
        this.annotatedType = annotatedType;
        this.annotations = annotations;
    }

    public static ReturnTypeDefinition load(Method method) {
        Assert.notNull(method, "method cannot be null.");
        return CACHE.computeIfAbsent(method, m -> loadInternal(m));
    }

    private static ReturnTypeDefinition loadInternal(Method method) {
        Description descriptionAnnotation = method.getAnnotatedReturnType().getAnnotation(Description.class);
        return new ReturnTypeDefinition(descriptionAnnotation != null ? descriptionAnnotation.value() : null,
                method.getGenericReturnType(), method.getAnnotatedReturnType(), method.getDeclaredAnnotations());
    }

    public String getDescription() {
        return description;
    }

    public Type getType() {
        return type;
    }

    public AnnotatedType getAnnotatedType() {
        return annotatedType;
    }

    public Annotation[] getAnnotations() {
        return annotations;
    }
}
