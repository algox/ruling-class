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

package org.algorithmx.rulii.traverse.types;

import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;
import org.algorithmx.rulii.validation.rules.pattern.Pattern;
import org.algorithmx.rulii.validation.types.ValidationAnnotatedTypeSupport;

import java.lang.reflect.AnnotatedArrayType;
import java.lang.reflect.AnnotatedParameterizedType;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.AnnotatedWildcardType;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnnotatedTypeDefinitionBuilder {

    public AnnotatedTypeDefinitionBuilder() {
        super();
    }

    public AnnotatedTypeDefinition build(AnnotatedType annotatedType, AnnotatedTypeSupport typeSupport) {
        Assert.notNull(typeSupport, "typeSupport cannot be null.");
        return traverseInternal(annotatedType, typeSupport);
    }

    protected AnnotatedTypeDefinition traverseInternal(AnnotatedType annotatedType, AnnotatedTypeSupport typeSupport) {

        AnnotatedTypeDefinition result;

        if (annotatedType instanceof AnnotatedParameterizedType) {
            result = visit((AnnotatedParameterizedType) annotatedType, typeSupport);
        } else if (annotatedType instanceof AnnotatedWildcardType) {
            result = visit((AnnotatedWildcardType) annotatedType, typeSupport);
        } else if (annotatedType instanceof AnnotatedTypeVariable) {
            result = visit((AnnotatedTypeVariable) annotatedType, typeSupport);
        } else if (annotatedType instanceof AnnotatedArrayType) {
            result = visit((AnnotatedArrayType) annotatedType, typeSupport);
        } else {
            result = visit(annotatedType, typeSupport);
        }

        return result;
    }

    protected AnnotatedParameterizedTypeDefinition visit(AnnotatedParameterizedType annotatedType, AnnotatedTypeSupport typeSupport) {
        List<AnnotatedTypeDefinition> typeArguments = new ArrayList<>();

        for (AnnotatedType type : annotatedType.getAnnotatedActualTypeArguments()) {
            typeArguments.add(traverseInternal(type, typeSupport));
        }

        return new AnnotatedParameterizedTypeDefinition(annotatedType, typeSupport.extractRuleAnnotations(annotatedType),
                typeSupport.extractTraversalAnnotation(annotatedType),
                typeArguments.toArray(new AnnotatedTypeDefinition[typeArguments.size()]));
    }

    protected AnnotatedWildcardTypeDefinition visit(AnnotatedWildcardType annotatedWildcardType, AnnotatedTypeSupport typeSupport) {
        List<AnnotatedTypeDefinition> bounds = new ArrayList<>();

        for (AnnotatedType type : annotatedWildcardType.getAnnotatedLowerBounds()) {
            bounds.add(traverseInternal(type, typeSupport));
        }

        for (AnnotatedType type : annotatedWildcardType.getAnnotatedUpperBounds()) {
            bounds.add(traverseInternal(type, typeSupport));
        }

        return new AnnotatedWildcardTypeDefinition(annotatedWildcardType,
                typeSupport.extractRuleAnnotations(annotatedWildcardType),
                typeSupport.extractTraversalAnnotation(annotatedWildcardType),
                bounds.toArray(new AnnotatedTypeDefinition[bounds.size()]));
    }

    protected AnnotatedTypeVariableDefinition visit(AnnotatedTypeVariable annotatedTypeVariable, AnnotatedTypeSupport typeSupport) {
        List<AnnotatedTypeDefinition> bounds = new ArrayList<>();

        for (AnnotatedType type : annotatedTypeVariable.getAnnotatedBounds()) {
            bounds.add(traverseInternal(type, typeSupport));
        }

        return new AnnotatedTypeVariableDefinition(annotatedTypeVariable,
                typeSupport.extractRuleAnnotations(annotatedTypeVariable),
                typeSupport.extractTraversalAnnotation(annotatedTypeVariable),
                bounds.toArray(new AnnotatedTypeDefinition[bounds.size()]));
    }

    protected AnnotatedArrayTypeDefinition visit(AnnotatedArrayType annotatedArrayType, AnnotatedTypeSupport typeSupport) {
        AnnotatedTypeDefinition componentType = traverseInternal(annotatedArrayType.getAnnotatedGenericComponentType(), typeSupport);
        return new AnnotatedArrayTypeDefinition(annotatedArrayType,
                typeSupport.extractRuleAnnotations(annotatedArrayType),
                typeSupport.extractTraversalAnnotation(annotatedArrayType), componentType);
    }

    protected SimpleAnnotatedTypeDefinition visit(AnnotatedType annotatedType, AnnotatedTypeSupport typeSupport) {
        return new SimpleAnnotatedTypeDefinition(annotatedType, typeSupport.extractTraversalAnnotation(annotatedType),
                typeSupport.extractRuleAnnotations(annotatedType));
    }

    @Validate
    @Pattern(regex = "*")
    private Map<@NotNull String, @NotNull Map<@NotNull @Validate ? extends List, @Validate @NotNull ?>> field1 = new HashMap<>();
    //private Map<@NotNull String, @NotNull Map<@NotNull Integer, @NotNull BigDecimal>> field1 = new HashMap<>();
    //private Map<@NotNull ?, @NotBlank ?> field1 = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Field field = AnnotatedTypeDefinitionBuilder.class.getDeclaredField("field1");
        AnnotatedType annotatedType = field.getAnnotatedType();
        AnnotatedTypeSupport support = new ValidationAnnotatedTypeSupport();
        AnnotatedTypeDefinitionBuilder builder = new AnnotatedTypeDefinitionBuilder();
        AnnotatedTypeDefinition result = builder.build(annotatedType, support);
        System.err.println(result.getSignature());
    }
}
