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
import org.algorithmx.rulii.annotation.Validate;
import org.algorithmx.rulii.annotation.ValidationMarker;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotation;
import org.algorithmx.rulii.lib.spring.core.annotation.MergedAnnotations;
import org.algorithmx.rulii.lib.spring.util.Assert;
import org.algorithmx.rulii.validation.rules.notnull.NotNull;
import org.algorithmx.rulii.validation.rules.pattern.Pattern;

import java.lang.annotation.Annotation;
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

public class AnnotatedTypeDefinitionBuilder<T> {

    private final AnnotatedType annotatedType;
    private final Class<? extends Annotation> introspectionAnnotationType;
    private final Class<? extends Annotation> markerAnnotationType;

    private AnnotatedTypeDefinitionBuilder(AnnotatedType annotatedType,
                                           Class<? extends Annotation> introspectionAnnotationType,
                                           Class<? extends Annotation> markerAnnotationType) {
        super();
        Assert.notNull(annotatedType, "annotatedType cannot be null.");
        Assert.notNull(markerAnnotationType, "markerAnnotationType cannot be null.");
        this.annotatedType = annotatedType;
        this.introspectionAnnotationType = introspectionAnnotationType;
        this.markerAnnotationType = markerAnnotationType;
    }

    public static AnnotatedTypeDefinitionBuilder with(AnnotatedType annotatedType,
                                                      Class<? extends Annotation> introspectionAnnotationType,
                                                      Class<? extends Annotation> markerAnnotationType) {
        return new AnnotatedTypeDefinitionBuilder(annotatedType, introspectionAnnotationType, markerAnnotationType);
    }

    public AnnotatedTypeDefinition build() {
        return traverseInternal(annotatedType);
    }

    protected AnnotatedTypeDefinition traverseInternal(AnnotatedType annotatedType) {

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

        return result;
    }

    protected AnnotatedParameterizedTypeDefinition visit(AnnotatedParameterizedType annotatedType) {
        List<AnnotatedTypeDefinition> typeArguments = new ArrayList<>();

        for (AnnotatedType type : annotatedType.getAnnotatedActualTypeArguments()) {
            typeArguments.add(traverseInternal(type));
        }

        return new AnnotatedParameterizedTypeDefinition(annotatedType,
                extractExtractAnnotation(annotatedType),
                extractIntrospectionAnnotation(annotatedType),
                extractMarkedAnnotations(annotatedType),
                typeArguments.toArray(new AnnotatedTypeDefinition[typeArguments.size()]));
    }

    protected AnnotatedWildcardTypeDefinition visit(AnnotatedWildcardType annotatedWildcardType) {
        List<AnnotatedTypeDefinition> lowerbounds = new ArrayList<>();
        List<AnnotatedTypeDefinition> upperbounds = new ArrayList<>();

        for (AnnotatedType type : annotatedWildcardType.getAnnotatedLowerBounds()) {
            lowerbounds.add(traverseInternal(type));
        }

        for (AnnotatedType type : annotatedWildcardType.getAnnotatedUpperBounds()) {
            upperbounds.add(traverseInternal(type));
        }

        return new AnnotatedWildcardTypeDefinition(annotatedWildcardType,
                extractExtractAnnotation(annotatedWildcardType),
                extractIntrospectionAnnotation(annotatedWildcardType),
                extractMarkedAnnotations(annotatedWildcardType),
                lowerbounds.toArray(new AnnotatedTypeDefinition[lowerbounds.size()]),
                upperbounds.toArray(new AnnotatedTypeDefinition[lowerbounds.size()]));
    }

    protected AnnotatedTypeVariableDefinition visit(AnnotatedTypeVariable annotatedTypeVariable) {
        List<AnnotatedTypeDefinition> bounds = new ArrayList<>();

        for (AnnotatedType type : annotatedTypeVariable.getAnnotatedBounds()) {
            bounds.add(traverseInternal(type));
        }

        return new AnnotatedTypeVariableDefinition(annotatedTypeVariable,
                extractExtractAnnotation(annotatedTypeVariable),
                extractIntrospectionAnnotation(annotatedTypeVariable),
                extractMarkedAnnotations(annotatedTypeVariable),
                bounds.toArray(new AnnotatedTypeDefinition[bounds.size()]));
    }

    protected AnnotatedArrayTypeDefinition visit(AnnotatedArrayType annotatedArrayType) {
        AnnotatedTypeDefinition componentType = traverseInternal(annotatedArrayType.getAnnotatedGenericComponentType());
        return new AnnotatedArrayTypeDefinition(annotatedArrayType, componentType);
    }

    protected SimpleAnnotatedTypeDefinition visit(AnnotatedType annotatedType) {
        return new SimpleAnnotatedTypeDefinition(annotatedType,
                extractExtractAnnotation(annotatedType),
                extractIntrospectionAnnotation(annotatedType),
                extractMarkedAnnotations(annotatedType));
    }

    protected <T extends Annotation> T extractIntrospectionAnnotation(AnnotatedType annotatedType) {
        if (introspectionAnnotationType == null) return null;
        MergedAnnotation<?> mergedAnnotation = MergedAnnotations.from(annotatedType).get(introspectionAnnotationType);
        return mergedAnnotation != null && mergedAnnotation.isPresent() ? (T) mergedAnnotation.synthesize() : null;
    }

    protected Extract extractExtractAnnotation(AnnotatedType annotatedType) {
        MergedAnnotation<?> mergedAnnotation = MergedAnnotations.from(annotatedType).get(Extract.class);
        return mergedAnnotation != null && mergedAnnotation.isPresent() ? (Extract) mergedAnnotation.synthesize() : null;
    }


    protected MarkedAnnotation[] extractMarkedAnnotations(AnnotatedType annotatedType) {
        List<MarkedAnnotation> result = new ArrayList<>();

        MergedAnnotations.from(annotatedType, MergedAnnotations.SearchStrategy.DIRECT)
                .stream(markerAnnotationType)
                .forEach(a -> {
                    result.add(new MarkedAnnotation(a.synthesize(), a.getMetaSource().synthesize()));
                });

        return result.toArray(new MarkedAnnotation[result.size()]);
    }

    @Validate
    @Pattern(regex = "*")
    private Map<@NotNull String, @NotNull Map<@NotNull @Validate ? extends List, @Validate @NotNull T>> field1 = new HashMap<>();
    //private Map<@NotNull String, @NotNull Map<@NotNull Integer, @NotNull BigDecimal>> field1 = new HashMap<>();
    //private Map<@NotNull ?, @NotBlank ?> field1 = new HashMap<>();

    public static void main(String[] args) throws Exception {
        Field field = AnnotatedTypeDefinitionBuilder.class.getDeclaredField("field1");
        AnnotatedType annotatedType = field.getAnnotatedType();
        AnnotatedTypeDefinitionBuilder builder = AnnotatedTypeDefinitionBuilder.with(annotatedType, Validate.class, ValidationMarker.class);
        AnnotatedTypeDefinition result = builder.build();
        System.err.println(result.getSignature());
    }
}
