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

import org.algorithmx.rulii.annotation.Extract;
import org.algorithmx.rulii.lib.spring.util.Assert;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnnotatedParameterizedTypeDefinition extends AbstractAnnotatedTypeDefinition<AnnotatedParameterizedType> {

    private final AnnotatedTypeDefinition[] typeArguments;

    public AnnotatedParameterizedTypeDefinition(AnnotatedParameterizedType annotatedType,
                                                Extract extract,
                                                Annotation traversalAnnotation,
                                                MarkedAnnotation[] ruleAnnotations,
                                                AnnotatedTypeDefinition...typeArguments) {
        super(annotatedType, AnnotatedTypeKind.PARAMETERIZED_TYPE,
                extract,
                traversalAnnotation,
                ruleAnnotations,
                childrenHaveRuleAnnotations(typeArguments),
                childrenRequireIntrospection(typeArguments));
        Assert.notNull(typeArguments, "typeArguments cannot be null.");
        this.typeArguments = typeArguments;
        establishParent(this, typeArguments);
    }

    public AnnotatedTypeDefinition[] getTypeArguments() {
        return typeArguments;
    }

    @Override
    public AnnotatedTypeDefinition[] getAllChildren() {
        List<AnnotatedTypeDefinition> result = new ArrayList<>();
        result.addAll(Arrays.asList(typeArguments));

        Arrays.stream(typeArguments)
                .filter(t -> t != null)
                .forEach(t -> result.addAll(Arrays.asList(t.getAllChildren())));

        return result.toArray(new AnnotatedTypeDefinition[result.size()]);
    }

    private static Class<?> getRawType(AnnotatedParameterizedType annotatedType) {
        ParameterizedType parameterizedType = (ParameterizedType) annotatedType.getType();
        return (Class<?>) parameterizedType.getRawType();
    }

    private static Type getOwnerType(AnnotatedParameterizedType annotatedType) {
        ParameterizedType parameterizedType = (ParameterizedType) annotatedType.getType();
        return parameterizedType.getOwnerType();
    }

    @Override
    public String toString() {
        return "ParameterizedTypeDefinition{" +
                super.toString() +
                ", typeArguments=" + Arrays.toString(typeArguments) +
                '}';
    }
}
