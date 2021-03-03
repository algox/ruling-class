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

import org.algorithmx.rulii.validation.rules.notnull.NotNull;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedTypeDefinitionBuilder {

    // TODO : Check whether we could have a cyclical dependency ?
    private final IdentityHashMap<AnnotatedType, AnnotatedTypeDefinition> breadCrumbs = new IdentityHashMap<>();

    public AnnotatedTypeDefinitionBuilder() {
        super();
    }

    public AnnotatedTypeDefinition build(AnnotatedType annotatedType) {
        breadCrumbs.clear();
        return traverse(annotatedType);
    }

    private AnnotatedTypeDefinition traverse(AnnotatedType annotatedType) {

        // TODO : Is this possible?
        if (breadCrumbs.containsKey(annotatedType)) return breadCrumbs.get(annotatedType);

        AnnotatedTypeDefinition result;

        if (annotatedType instanceof AnnotatedParameterizedType) {
            result = visit((AnnotatedParameterizedType) annotatedType);
        } else if (annotatedType instanceof AnnotatedWildcardType) {
            result = visit((AnnotatedWildcardType) annotatedType);
        } else if (annotatedType instanceof AnnotatedTypeVariable) {
            result = visit((AnnotatedTypeVariable) annotatedType);
        } else if (annotatedType instanceof AnnotatedArrayType) {
            result = visit((AnnotatedArrayType) annotatedType);
        } else {
            result = visit(annotatedType);
        }

        breadCrumbs.put(annotatedType, result);

        return result;
    }

    private AnnotatedParameterizedTypeDefinition visit(AnnotatedParameterizedType annotatedType) {
        List<AnnotatedTypeDefinition> typeArguments = new ArrayList<>();

        for (AnnotatedType type : annotatedType.getAnnotatedActualTypeArguments()) {
            typeArguments.add(traverse(type));
        }

        return new AnnotatedParameterizedTypeDefinition(annotatedType, typeArguments.toArray(new AnnotatedTypeDefinition[typeArguments.size()]));
    }

    private AnnotatedWildcardTypeDefinition visit(AnnotatedWildcardType annotatedWildcardType) {
        List<AnnotatedTypeDefinition> bounds = new ArrayList<>();

        for (AnnotatedType type : annotatedWildcardType.getAnnotatedLowerBounds()) {
            bounds.add(traverse(type));
        }

        for (AnnotatedType type : annotatedWildcardType.getAnnotatedUpperBounds()) {
            bounds.add(traverse(type));
        }

        return new AnnotatedWildcardTypeDefinition(annotatedWildcardType, bounds.toArray(new AnnotatedTypeDefinition[bounds.size()]));
    }

    private AnnotatedTypeVariableDefinition visit(AnnotatedTypeVariable annotatedTypeVariable) {
        List<AnnotatedTypeDefinition> bounds = new ArrayList<>();

        for (AnnotatedType type : annotatedTypeVariable.getAnnotatedBounds()) {
            bounds.add(traverse(type));
        }

        return new AnnotatedTypeVariableDefinition(annotatedTypeVariable, bounds.toArray(new AnnotatedTypeDefinition[bounds.size()]));
    }

    private AnnotatedArrayTypeDefinition visit(AnnotatedArrayType annotatedArrayType) {
        AnnotatedTypeDefinition componentType = traverse(annotatedArrayType.getAnnotatedGenericComponentType());
        return new AnnotatedArrayTypeDefinition(annotatedArrayType,  componentType);
    }

    private SimpleAnnotatedTypeDefinition visit(AnnotatedType annotatedType) {
        return new SimpleAnnotatedTypeDefinition(annotatedType);
    }

    private Map<@NotNull String, @NotNull Map<@NotNull ? extends List, @NotNull ?>> field1 = new HashMap<>();
    //private Map<@NotNull String, @NotNull Map<@NotNull Integer, @NotNull BigDecimal>> field1 = new HashMap<>();
    //private Map<@NotNull ?, @NotBlank ?> field1 = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Field field = AnnotatedTypeDefinitionBuilder.class.getDeclaredField("field1");
        AnnotatedType annotatedType = field.getAnnotatedType();
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition result = builder.build(annotatedType);
        System.err.println(result.getSignature());
    }
}
