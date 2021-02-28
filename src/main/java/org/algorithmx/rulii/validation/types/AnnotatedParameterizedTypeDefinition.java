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

import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

public class AnnotatedParameterizedTypeDefinition extends AbstractAnnotatedTypeDefinition<AnnotatedParameterizedType> {

    private final Class<?> type;
    private final AnnotatedTypeDefinition[] typeArguments;

    public AnnotatedParameterizedTypeDefinition(AnnotatedParameterizedType annotatedType, AnnotatedTypeDefinition...typeArguments) {
        super(annotatedType, AnnotatedTypeKind.PARAMETERIZED_TYPE, deriveRequiresTraversal(typeArguments));
        Assert.notNull(typeArguments, "typeArguments cannot be null.");
        this.type = deriveType(annotatedType);
        this.typeArguments = typeArguments;
    }

    public Class<?> getType() {
        return type;
    }

    public AnnotatedTypeDefinition[] getTypeArguments() {
        return typeArguments;
    }

    @Override
    public String getSignature() {
        StringBuilder result = new StringBuilder(type.getSimpleName() + "<");

        for (int i = 0; i < typeArguments.length; i++) {
            result.append(typeArguments[i].getSignature());
            if (i < typeArguments.length - 1) result.append(",");
        }
        result.append(">");

        return result.toString();
    }

    private static Class<?> deriveType(AnnotatedParameterizedType annotatedType) {
        ParameterizedType parameterizedType = (ParameterizedType) annotatedType.getType();
        return (Class<?>) parameterizedType.getRawType();
    }

    @Override
    public String toString() {
        return "ParameterizedTypeDefinition{" +
                "type=" + type +
                ", typeArguments=" + Arrays.toString(typeArguments) +
                '}';
    }
}
